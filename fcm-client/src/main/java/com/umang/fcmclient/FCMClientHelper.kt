package com.umang.fcmclient

import android.app.Application
import android.content.Context
import com.google.firebase.messaging.RemoteMessage
import com.umang.logger.SmartLogHelper
import com.umang.logger.SmartLogger
import com.umang.logger.verbose
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * @author Umang Chamaria
 */
class FCMClientHelper internal constructor(private var context: Context): SmartLogger {

  private var tokenReceivedListener: ConcurrentLinkedQueue<TokenReceivedListener> = ConcurrentLinkedQueue()

  private var pushReceivedListener: ConcurrentLinkedQueue<PushReceivedListener> = ConcurrentLinkedQueue()

  fun registerTokenRegistrationListener(tokenReceivedListener: TokenReceivedListener) {
    this.tokenReceivedListener.add(tokenReceivedListener)
  }

  fun registerPushReceivedListener(pushReceivedListener: PushReceivedListener){
    this.pushReceivedListener.add(pushReceivedListener)
  }

  @Synchronized
  fun onTokenRegistered(token: String) {
    if (SharedPref.newInstance(context).pushToken.equals(token))return
    tokenReceivedListener.forEach {
      it.onTokenReceived(token)
    }
  }

  fun initializeFCMClient(application: Application) {
    application.registerActivityLifecycleCallbacks(FCMClientLibActivityLifecycleCallbacks())
  }

  @Synchronized
  fun onPushReceived(remoteMessage: RemoteMessage){
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

  fun subscribeToTopics(topics: Array<String>) {
    FCMClientLibWorker.subscribeToTopic(context, topics)
    SharedPref.newInstance(context).topics = topics.toSet()
  }

  fun enableLogs(context: Context){
    SmartLogHelper.initializeLogger(context)
    SmartLogHelper.LOG_LEVEL = SmartLogHelper.LOG_LEVEL_VERBOSE
  }

  fun disableLogs(){
    SmartLogHelper.LOG_STATUS = false
  }

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

  private fun getSubscribedTopics(): Array<String>{
    val subscribedSet = SharedPref.newInstance(context).topics
    val subscribedArray: MutableList<String> = mutableListOf()
    subscribedSet?.forEach{
      subscribedArray.add(it)
    }
    return subscribedArray.toTypedArray()
  }

  companion object {
    private var activityCounter = 0

    fun newInstance(context: Context): FCMClientHelper {
      return FCMClientHelper(context)
    }
  }

  interface TokenReceivedListener {
    fun onTokenReceived(token: String)
  }

  interface PushReceivedListener {
    fun onPushReceived(remoteMessage: RemoteMessage)
  }
}