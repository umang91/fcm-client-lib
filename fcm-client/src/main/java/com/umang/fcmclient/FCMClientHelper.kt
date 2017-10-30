package com.umang.fcmclient

import android.app.Application
import android.content.Context

/**
 * @author Umang Chamaria
 */
class FCMClientHelper internal constructor(private var context: Context) {

  private var tokenReceivedListener: TokenReceivedListener? = null

  fun registerTokenRegisterationCallbackListener(tokenReceivedListener: TokenReceivedListener) {
    this.tokenReceivedListener = tokenReceivedListener
  }

  fun onTokenRegistered(token: String) {
    tokenReceivedListener?.onTokenReceived(token)
  }

  fun enableRegistrationFallback(application: Application) {
    application.registerActivityLifecycleCallbacks(FCMClientLibActivityLifecycleCallbacks())

  }

  fun registerForPushIfRequired(){
    FCMClientLibWorker.registerForPush(context)
  }


  internal fun onStart(){
    if (activityCounter == 0){
      registerForPushIfRequired()
    }
    activityCounter++
  }

  internal fun onStop(){
    activityCounter--
    if (activityCounter == 0){
      registerForPushIfRequired()
    }
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
}