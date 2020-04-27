package com.umang.example.fcmclient

import android.app.Application
import com.umang.fcmclient.FcmClientHelper

/**
 * @author Umang Chamaria
 */
class ExampleApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    //enable sdk logs
    FcmClientHelper.getInstance(applicationContext).enableLogs()
    // initialize sdk
    FcmClientHelper.getInstance(applicationContext).initialise(this)
    //set push token callback listener
  }
}