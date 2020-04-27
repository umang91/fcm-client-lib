package com.umang.fcmclient

import android.app.Application
import android.content.Context
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.umang.fcmclient.listeners.FcmCallback
import com.umang.fcmclient.repository.Repository
import com.umang.logger.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Main interaction class for the FCM client
 * @author Umang Chamaria
 */
class FcmClientHelper internal constructor(private var context: Context) : SmartLogger {

  private var activityCounter = 0

  private val listeners = mutableListOf<FcmCallback>()

  private var scheduledExecutor = Executors.newScheduledThreadPool(1)

  private val isAppInForeground: Boolean
    get() = activityCounter > 0

  private val repository = Repository(context)

  /**
   * Register a callback for FCM events.
   * @param listener instance of [FcmCallback]
   */
  @Suppress("SENSELESS_COMPARISON")
  fun addListener(listener: FcmCallback) {
    if (listener == null) return
    listeners.add(listener)
  }

  /**
   * Remove of a registered callback for FCM events
   * @param listener instance of registered [FcmCallback]
   */
  @Suppress("SENSELESS_COMPARISON")
  fun removeListener(listener: FcmCallback) {
    if (listener == null) return
    listeners.remove(listener)
  }

  /**
   * Initialize the FCM client. This needs to be called from the onCreate() of [Application] class.
   * @param application instance of the [Application]
   *
   * ```
   *
   * FCMClientHelper.getInstance(applicationContext).initialise(this)
   *
   * ```
   */
  fun initialise(application: Application) {
    application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks())
    SmartLogHelper.LOG_TAG = "FCMClient_v$LIBRARY_VERSION"
    SmartLogHelper.initializeLogger(context)
  }

  @Synchronized
  internal fun onPushReceived(remoteMessage: RemoteMessage) {
    for (listener in listeners) {
      try {
        listener.onPushReceived(remoteMessage)
      } catch (e: Exception) {

      }
    }
  }

  /**
   * Subscribe to the given list of topics
   * @param topics list of topics to subscribe
   */
  fun subscribeToTopics(topics: List<String>) {
    AsyncExecutor.submit {
      for (topic in topics) {
        verbose("Subscribing to $topic")
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
      }
    }
  }

  internal fun onStart() {
    if (activityCounter == 0) {
      registerForPush()
    }
    activityCounter++
    verbose("onStart(): activity counter: $activityCounter")
  }

  internal fun onStop() {
    activityCounter--
    verbose("onStop(): activity counter: $activityCounter")
    if (activityCounter == 0) {
      onAppBackground()
    }
  }

  /**
   * To un-subscribe from messaging topics.
   * @param topics list of topics to be un-subscribed from.
   */
  fun unSubscribeTopic(topics: List<String>) {
    AsyncExecutor.submit {
      for (topic in topics) {
        verbose("Un-subscribing to $topic")
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
      }
    }
  }

  internal fun onNewToken(token: String) {
    notifyListenersIfRequired(token)
    repository.saveToken(token)
  }

  private fun registerForPushIfRequired() {
    val savedToken: String = repository.getToken()
    if (savedToken.isEmpty()) {
      registerForPush()
    }
  }

  private fun onAppBackground() {
    shutdownRetryScheduler()
    registerForPushIfRequired()
  }

  private fun registerForPush() {
    FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
      try {
        if (!task.isSuccessful) {
          scheduleRetry()
          return@addOnCompleteListener
        }
        val token = task.result?.token
        if (token.isNullOrEmpty()) {
          scheduleRetry()
          return@addOnCompleteListener
        }
        info("Push Token: $token")
        onNewToken(token)
      } catch (e: Exception) {
        error("registerForPush(): Exception: ", e)
        scheduleRetry()
      }
    }
  }

  private fun scheduleRetry() {
    if (!isAppInForeground) return
    if (scheduledExecutor.isShutdown) {
      scheduledExecutor = Executors.newScheduledThreadPool(1)
    }
    scheduledExecutor.schedule({ registerForPush() }, 1, TimeUnit.MINUTES)
  }

  private fun shutdownRetryScheduler() {
    if (!scheduledExecutor.isShutdown) scheduledExecutor.shutdownNow()
  }

  private fun notifyListenersIfRequired(token: String) {
    AsyncExecutor.submit {
      val savedToken = repository.getToken()
      if (token == savedToken) return@submit
      verbose(" notifyListenersIfRequired() : Notifying listeners")
      for (listener in listeners) {
        try {
          listener.onTokenAvailable(token)
        } catch (e: Exception) {

        }
      }
    }
  }

  companion object {
    private var instance: FcmClientHelper? = null

    /**
     * Get instance of [FcmClientHelper]
     * @param context instance of [Context]
     * @return instance of [FcmClientHelper]
     */
    fun getInstance(context: Context): FcmClientHelper {
      if (instance == null) {
        instance = FcmClientHelper(context)
      }
      return instance as FcmClientHelper
    }
  }
}

const val LIBRARY_VERSION = 1100