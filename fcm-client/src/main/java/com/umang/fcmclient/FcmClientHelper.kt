package com.umang.fcmclient

import android.app.Application
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.umang.fcmclient.listeners.FirebaseMessageListener
import com.umang.fcmclient.repository.Provider
import com.umang.fcmclient.util.Logger
import com.umang.fcmclient.util.isDebugBuild
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Main interaction class for the FCM client
 * @author Umang Chamaria
 */
public class FcmClientHelper internal constructor(private var context: Context) {

    private val logger = Logger.getLogger("FcmClientHelper")

    private val listeners = mutableListOf<FirebaseMessageListener>()

    private var scheduledExecutor = Executors.newScheduledThreadPool(1)

    private var isAppInForeground: Boolean = false

    private var retryInterval: Long = 30

    /**
     * Register a callback for FCM events.
     * @param listener instance of [FirebaseMessageListener]
     */
    @Suppress("SENSELESS_COMPARISON")
    public fun addListener(listener: FirebaseMessageListener) {
        if (listener == null) return
        listeners.add(listener)
    }

    /**
     * Remove of a registered callback for FCM events
     * @param listener instance of registered [FirebaseMessageListener]
     */
    @Suppress("SENSELESS_COMPARISON")
    public fun removeListener(listener: FirebaseMessageListener) {
        if (listener == null) return
        listeners.remove(listener)
    }

    /**
     * Initialize the FCM client. This needs to be called from the onCreate() of [Application] class.
     * @param application instance of the [Application]
     * @param logLevel optional parameter takes in the level of logs which should be printed by the
     * SDK when the application is running in debug mode. By default, only error logs are printed.
     * Refer to [Logger.LogLevel] for more details.
     * @param retryInterval optional parameter which takes the time interval(in seconds) after
     * which the SDK should retry registering for Push Token in case of any failure. Default
     * value is 30 seconds.
     *
     */
    @Deprecated(
        "This API has been deprecated since 2.1.00",
        ReplaceWith(
            "FcmClientHelper.getInstance(applicationContext).initialise()",
            "com.umang.fcmclient.FcmClientHelper"
        ),
        DeprecationLevel.WARNING
    )
    public fun initialise(
        application: Application,
        logLevel: Logger.LogLevel = Logger.LogLevel.ERROR,
        retryInterval: Long = 30
    ) {
        try {
            initialise(logLevel, retryInterval)
        } catch (e: Exception) {
            logger.error(" initialise() ", e)
        }
    }

    /**
     * Initialize the FCM client. This needs to be called from the onCreate() of [Application] class.
     *
     * @param logLevel optional parameter takes in the level of logs which should be printed by the
     * SDK when the application is running in debug mode. By default, only error logs are printed.
     * Refer to [Logger.LogLevel] for more details.
     * @param retryInterval optional parameter which takes the time interval(in seconds) after
     * which the SDK should retry registering for Push Token in case of any failure. Default
     * value is 30 seconds.
     */
    public fun initialise(
        logLevel: Logger.LogLevel = Logger.LogLevel.ERROR,
        retryInterval: Long = 30
    ) {
        try {
            ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver(context.applicationContext))
            Logger.logLevel = logLevel
            Logger.isLogEnabled = isDebugBuild(context.applicationContext)
            if (retryInterval >= 5) {
                this.retryInterval = retryInterval
            }
            logger.verbose(" initialise() Initialising fcm client library. Log level - $logLevel")
        } catch (e: Exception) {
            logger.error(" initialise() ", e)
        }
    }

    @Synchronized
    internal fun onPushReceived(remoteMessage: RemoteMessage) {
        for (listener in listeners) {
            try {
                listener.onPushReceived(remoteMessage)
            } catch (e: Exception) {
                logger.error("onPushReceived() ", e)
            }
        }
    }

    /**
     * Subscribe to the given list of topics
     * @param topics list of topics to subscribe
     */
    public fun subscribeToTopics(topics: List<String>) {
        AsyncExecutor.submit {
            try {
                for (topic in topics) {
                    logger.verbose("Subscribing to $topic")
                    FirebaseMessaging.getInstance().subscribeToTopic(topic)
                        .addOnCompleteListener { task ->
                            logger.verbose("subscribeToTopic() isSuccess ${task.isSuccessful}")
                        }
                }
            } catch (e: Exception) {
                logger.error(" subscribeToTopic() ", e)
            }
        }
    }

    /**
     * To un-subscribe from messaging topics.
     * @param topics list of topics to be un-subscribed from.
     */
    public fun unSubscribeTopic(topics: List<String>) {
        AsyncExecutor.submit {
            try {
                for (topic in topics) {
                    logger.verbose("Un-subscribing to $topic")
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                }
            } catch (e: Exception) {
                logger.error(" unSubscribeTopic() ", e)
            }
        }
    }

    internal fun onNewToken(token: String) {
        AsyncExecutor.submit {
            try {
                Provider.getRepository(context).saveToken(token)
                notifyListeners(token)
            } catch (e: Exception) {
                logger.error(" onNewToken() ", e)
            }
        }
    }

    private fun registerForPushIfRequired() {
        val savedToken = Provider.getRepository(context).getToken()
        if (savedToken.isEmpty()) {
            registerForPush()
        }
    }

    internal fun onAppForeGround() {
        try {
            logger.verbose("onAppForeGround(): Application coming to foreground.")
            registerForPush()
            isAppInForeground = true
        } catch (e: Exception) {
            logger.error(" onAppForeGround(): Exception: ", e)
        }
    }

    internal fun onAppBackground() {
        try {
            logger.verbose("onAppBackground(): Application going to background.")
            isAppInForeground = false
            shutdownRetryScheduler()
            registerForPushIfRequired()
        } catch (e: Exception) {
            logger.error(" onAppBackground(): Exception: ", e)
        }
    }

    private fun registerForPush() {
        logger.verbose(" registerForPush(): Will register for push.")
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            try {
                if (!task.isSuccessful) {
                    logger.verbose(" registerForPush(): Token registration failed.")
                    scheduleRetry()
                    return@addOnCompleteListener
                }
                val token = task.result
                if (token.isNullOrEmpty()) {
                    logger.verbose(" registerForPush(): Token null or empty.")
                    scheduleRetry()
                    return@addOnCompleteListener
                }
                logger.info(" registerForPush() Token: $token")
                onNewToken(token)
            } catch (e: Exception) {
                logger.error("registerForPush(): ", e)
                scheduleRetry()
            }
        }
    }

    private fun scheduleRetry() {
        if (!isAppInForeground) return
        if (scheduledExecutor.isShutdown) {
            scheduledExecutor = Executors.newScheduledThreadPool(1)
        }
        logger.verbose(" scheduleRetry() Will schedule retry.")
        scheduledExecutor.schedule({ registerForPush() }, retryInterval, TimeUnit.SECONDS)
    }

    private fun shutdownRetryScheduler() {
        if (!scheduledExecutor.isShutdown) {
            logger.verbose(" shutdownRetryScheduler() Shutting down retry scheduler.")
            scheduledExecutor.shutdownNow()
        }
    }

    private fun notifyListeners(token: String) {
        try {
            logger.verbose(" notifyListenersIfRequired() : Notifying listeners")
            for (listener in listeners) {
                try {
                    listener.onTokenAvailable(token)
                } catch (e: Exception) {
                    logger.error(" notifyListenersIfRequired() : ", e)
                }
            }
        } catch (e: Exception) {
            logger.error(" notifyListenersIfRequired() ", e)
        }
    }

    public companion object {
        private var instance: FcmClientHelper? = null

        /**
         * Get instance of [FcmClientHelper]
         * @param context instance of [Context]
         * @return instance of [FcmClientHelper]
         */
        public fun getInstance(context: Context): FcmClientHelper {
            return instance ?: synchronized(FcmClientHelper::class.java){
                val inst = instance ?: FcmClientHelper(context)
                instance = inst
                inst
            }
        }
    }
}
