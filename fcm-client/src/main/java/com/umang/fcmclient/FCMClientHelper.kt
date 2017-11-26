package com.umang.fcmclient

import android.app.Application
import android.content.Context
import com.google.firebase.messaging.RemoteMessage
import com.umang.logger.SmartLogHelper
import com.umang.logger.SmartLogger
import com.umang.logger.verbose
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Main interaction class for the FCM client
 * @author Umang Chamaria
 */
class FCMClientHelper internal constructor(private var context: Context): SmartLogger {

  private var tokenReceivedListener: ConcurrentLinkedQueue<TokenReceivedListener> = ConcurrentLinkedQueue()

  private var pushReceivedListener: ConcurrentLinkedQueue<PushReceivedListener> = ConcurrentLinkedQueue()

  /**
   * Registration for a callback on push token registration or push token change.
   * Should be set in the application class, else token refresh callback could be missed.
   * @param  tokenReceivedListener instance of [TokenReceivedListener]
   */
  fun registerTokenRegistrationListener(tokenReceivedListener: TokenReceivedListener) {
    this.tokenReceivedListener.add(tokenReceivedListener)
  }

  /**
   * Registration for a callback whenever a push is received.
   * Should be implemented in the application class, else callback might be missed if push is
   * received when the process is killed or app is in background.
   * @param pushReceivedListener instance of [PushReceivedListener]
   */
  fun registerPushReceivedListener(pushReceivedListener: PushReceivedListener){
    this.pushReceivedListener.add(pushReceivedListener)
  }

  @Synchronized
  internal fun onTokenRegistered(token: String) {
    if (SharedPref.newInstance(context).pushToken.equals(token))return
    tokenReceivedListener.forEach {
      it.onTokenReceived(token)
    }
  }

  /**
   * Initialize the FCM client. This needs to be called from the onCreate() of [Application] class.
   * @param application instance of the [Application]
   */
  fun initializeFCMClient(application: Application) {
    application.registerActivityLifecycleCallbacks(FCMClientLibActivityLifecycleCallbacks())
    SmartLogHelper.LOG_TAG = "FCMClient_v1000"
    SmartLogHelper.initializeLogger(context)
  }

  @Synchronized
  internal fun onPushReceived(remoteMessage: RemoteMessage){
    pushReceivedListener.forEach{
      it.onPushReceived(remoteMessage)
    }
  }

  private fun registerForPushIfRequired() {
    val savedToken: String? = SharedPref.newInstance(context).pushToken
    if (savedToken == null) {
      forceRegisterForPush()
    }
  }

  /**
   * Subscribe to the given list of topics
   * @param topics list of topics to subscribe
   */
  fun subscribeToTopics(topics: List<String>) {
    FCMClientLibWorker.subscribeToTopic(context, topics)
    SharedPref.newInstance(context).topics = topics.toSet()
  }

  /**
   * Initialize verbose logging for the FCM client. By default only info logs are enabled.
   * This would only print logs if Build used is of type debug.
   * To disable logs check [disableLogs]
   * @param context instance of [Context]
   */
  fun enableLogs(){
    SmartLogHelper.LOG_LEVEL = SmartLogHelper.LOG_LEVEL_VERBOSE
  }

  /**
   * Disable all logs for FCM Client.
   */
  fun disableLogs(){
    SmartLogHelper.LOG_STATUS = false
  }

  /**
   * Enable verbose logging for singed builds. By default this is disabled and should be only set
   * for testing.
   */
  fun enableLogsForSignedBuild(){
    SmartLogHelper.LOG_STATUS = true
    SmartLogHelper.LOG_LEVEL = SmartLogHelper.LOG_LEVEL_VERBOSE
  }

  private fun forceRegisterForPush() {
    FCMClientLibWorker.registerForPush(context)
    subscribeToTopics(getSubscribedTopics())
  }

  internal fun onStart() {
    if (activityCounter == 0) {
      forceRegisterForPush()
    }
    registerForPushIfRequired()
    activityCounter++
    verbose("onStart(): activity counter: $activityCounter")
  }

  internal fun onStop() {
    activityCounter--
    verbose("onStop(): activity counter: $activityCounter")
    if (activityCounter == 0) {
      registerForPushIfRequired()
    }
  }

  internal fun refreshToken() {
    SharedPref.newInstance(context).pushToken = null
    FCMClientLibWorker.registerForPush(context)
  }

  private fun getSubscribedTopics(): List<String>{
    val subscribedSet = SharedPref.newInstance(context).topics
    val subscribedArray: MutableList<String> = mutableListOf()
    subscribedSet?.forEach{
      subscribedArray.add(it)
    }
    return subscribedArray
  }

  /**
   * To un-subscribe from messaging topics.
   * @param topics list of topics to be un-subscribed from.
   */
  fun unSubscribeTopic(topics: List<String>){
    val subscribedTopics = getSubscribedTopics().toMutableList()
    if (subscribedTopics.isNotEmpty()){
      topics.forEach{
        if (subscribedTopics.contains(it)){
          subscribedTopics.remove(it)
        }
      }
      updateSubscribedTopics(topics)
      FCMClientLibWorker.unsubscribeFromTopic(context, topics)
    }
  }

  private fun updateSubscribedTopics(topics: List<String>){
    SharedPref.newInstance(context).topics = topics.toSet()
  }

  companion object {
    private var activityCounter = 0

    private var instance: FCMClientHelper? = null
    /**
     * Get instance of [FCMClientHelper]
     * @param context instance of [Context]
     * @return instance of [FCMClientHelper]
     */
    fun newInstance(context: Context): FCMClientHelper {
      if (instance == null){
        instance = FCMClientHelper(context)
      }
      return instance!!
    }
  }

  /**
   * Callback interface for token registration.
   */
  interface TokenReceivedListener {
    /**
     * @param token Push token.
     */
    fun onTokenReceived(token: String)
  }

  /**
   * Callback interface for receiving push messages.
   * Should
   */
  interface PushReceivedListener {
    /**
     * @param remoteMessage Push payload received from FCM server
     */
    fun onPushReceived(remoteMessage: RemoteMessage)
  }
}