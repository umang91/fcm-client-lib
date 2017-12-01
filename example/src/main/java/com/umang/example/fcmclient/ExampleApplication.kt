package com.umang.example.fcmclient

import android.app.Application
import com.umang.fcmclient.FCMClientHelper

/**
 * @author Umang Chamaria
 */
class ExampleApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    FCMClientHelper.newInstance(applicationContext).enableLogs()
    FCMClientHelper.newInstance(applicationContext).initializeFCMClient(this)
    FCMClientHelper.newInstance(applicationContext).registerTokenRegistrationListener(TokenReceiver())
    FCMClientHelper.newInstance(applicationContext).registerTokenRegistrationListener(AnotherTokenReceiver())
    FCMClientHelper.newInstance(applicationContext).registerPushReceivedListener(PushReceiver())
  }
}