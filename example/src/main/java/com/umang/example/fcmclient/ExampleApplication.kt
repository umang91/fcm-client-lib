package com.umang.example.fcmclient

import android.app.Application
import com.umang.fcmclient.FcmClientHelper
import com.umang.fcmclient.util.Logger

/**
 * @author Umang Chamaria
 */
class ExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // initialize sdk
        FcmClientHelper.getInstance(applicationContext).initialise(this, Logger.LogLevel.VERBOSE)
    }
}