package com.umang.fcmclient

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
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

    private var retryInterval: Long = DEFAULT_RETRY_INTERVAL

    /**
     * Register a callback for FCM events.
     * @param listener instance of [FirebaseMessageListener]
     */
    public fun addListener(listener: FirebaseMessageListener) {
        listeners.add(listener)
    }

    /**
     * Remove of a registered callback for FCM events
     * @param listener instance of registered [FirebaseMessageListener]
     */
    public fun removeListener(listener: FirebaseMessageListener) {
        listeners.remove(listener)
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
    @JvmOverloads
    public fun initialise(
        logLevel: Logger.LogLevel = Logger.LogLevel.ERROR,
        retryInterval: Long = DEFAULT_RETRY_INTERVAL
    ) {
        try {
            setupLogging(logLevel)
            registerLifecycleCallback()
            if (retryInterval >= 5) {
                this.retryInterval = retryInterval
            }
            logger.log { " initialise() Initialising fcm client library. Log level - $logLevel" }
        } catch (e: Exception) {
            logger.log(Logger.LogLevel.ERROR, e) { " initialise() " }
        }
    }

    @Synchronized
    internal fun onPushReceived(remoteMessage: RemoteMessage) {
        for (listener in listeners) {
            try {
                listener.onPushReceived(remoteMessage)
            } catch (e: Exception) {
                logger.log(Logger.LogLevel.ERROR, e) { " onPushReceived() " }
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
                    logger.log { "Subscribing to $topic" }
                    FirebaseMessaging.getInstance().subscribeToTopic(topic)
                        .addOnCompleteListener { task ->
                            logger.log { "subscribeToTopic() isSuccess ${task.isSuccessful}" }
                        }
                }
            } catch (e: Exception) {
                logger.log(Logger.LogLevel.ERROR, e) { " subscribeToTopic()" }
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
                    logger.log { "Un-subscribing to $topic" }
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                }
            } catch (e: Exception) {
                logger.log(Logger.LogLevel.ERROR, e) { " unSubscribeTopic()" }
            }
        }
    }

    internal fun onNewToken(token: String) {
        AsyncExecutor.submit {
            try {
                Provider.getRepository(context).saveToken(token)
                notifyListeners(token)
            } catch (e: Exception) {
                logger.log(Logger.LogLevel.ERROR, e) { " onNewToken()" }
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
            logger.log { "onAppForeGround(): Application coming to foreground." }
            isAppInForeground = true
            registerForPush()
        } catch (e: Exception) {
            logger.log(Logger.LogLevel.ERROR, e) { " onAppForeGround()" }
        }
    }

    internal fun onAppBackground() {
        try {
            logger.log { "onAppBackground(): Application going to background." }
            isAppInForeground = false
            shutdownRetryScheduler()
            registerForPushIfRequired()
        } catch (e: Exception) {
            logger.log(Logger.LogLevel.ERROR, e) { " onAppBackground():" }
        }
    }

    private fun registerForPush() {
        logger.log { " registerForPush(): Will register for push." }
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            try {
                if (!task.isSuccessful) {
                    logger.log { " registerForPush(): Token registration failed." }
                    scheduleRetry()
                    return@addOnCompleteListener
                }
                val token = task.result
                if (token.isNullOrEmpty()) {
                    logger.log { " registerForPush(): Token null or empty." }
                    scheduleRetry()
                    return@addOnCompleteListener
                }
                logger.log(Logger.LogLevel.INFO) { " registerForPush() Token: $token" }
                onNewToken(token)
            } catch (e: Exception) {
                logger.log(Logger.LogLevel.ERROR, e) { "registerForPush(): " }
                scheduleRetry()
            }
        }
    }

    private fun scheduleRetry() {
        if (!isAppInForeground) return
        if (scheduledExecutor.isShutdown) {
            scheduledExecutor = Executors.newScheduledThreadPool(1)
        }
        logger.log { " scheduleRetry() Will schedule retry." }
        scheduledExecutor.schedule({ registerForPush() }, retryInterval, TimeUnit.SECONDS)
    }

    private fun shutdownRetryScheduler() {
        if (!scheduledExecutor.isShutdown) {
            logger.log(Logger.LogLevel.ERROR) { " shutdownRetryScheduler() Shutting down retry scheduler." }
            scheduledExecutor.shutdownNow()
        }
    }

    private fun notifyListeners(token: String) {
        try {
            logger.log { " notifyListenersIfRequired() : Notifying listeners" }
            for (listener in listeners) {
                try {
                    listener.onTokenAvailable(token)
                } catch (e: Exception) {
                    logger.log(Logger.LogLevel.ERROR, e) { " notifyListenersIfRequired() : " }
                }
            }
        } catch (e: Exception) {
            logger.log(Logger.LogLevel.ERROR, e) { " notifyListenersIfRequired() " }
        }
    }

    private fun setupLogging(logLevel: Logger.LogLevel) {
        Logger.logLevel = logLevel
        Logger.isLogEnabled = isDebugBuild(context.applicationContext)
    }

    private fun registerLifecycleCallback() {
        val mainThread = Handler(Looper.getMainLooper())
        mainThread.post {
            try {
                ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver(context.applicationContext))
            } catch (e: Exception) {
                logger.log(Logger.LogLevel.ERROR, e) { " registerLifecycleCallback() " }
            }
        }
    }

    public companion object {
        private var instance: FcmClientHelper? = null

        /**
         * Get instance of [FcmClientHelper]
         * @param context instance of [Context]
         * @return instance of [FcmClientHelper]
         */
        @JvmStatic
        public fun getInstance(context: Context): FcmClientHelper {
            return instance ?: synchronized(FcmClientHelper::class.java) {
                val inst = instance ?: FcmClientHelper(context)
                instance = inst
                inst
            }
        }
    }
}
