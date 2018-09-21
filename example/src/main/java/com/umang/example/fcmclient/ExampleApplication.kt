package com.umang.example.fcmclient

import android.app.Application
import com.umang.fcmclient.FCMClientHelper

/**
 * @author Umang Chamaria
 */
class ExampleApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    //enable sdk logs
    FCMClientHelper.newInstance(applicationContext).enableLogs()
    // initialize sdk
    FCMClientHelper.newInstance(applicationContext).initializeFCMClient(this)
    //set push token callback listener
    FCMClientHelper.newInstance(applicationContext).registerTokenRegistrationListener(TokenReceiver())
    //set another push token callback listener
    FCMClientHelper.newInstance(applicationContext).registerTokenRegistrationListener(AnotherTokenReceiver())
    //set push payload callback listener
    FCMClientHelper.newInstance(applicationContext).registerPushReceivedListener(PushReceiver())
  }
}