package dev.assemblage.fcm.client.example

import android.app.Application
import android.os.StrictMode
import dev.assemblage.fcm.client.FcmClientHelper
import dev.assemblage.fcm.client.internal.util.Logger

/**
 * @author Umang Chamaria
 */
class ExampleApplication : Application() {
    override fun onCreate() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork() // or .detectAll() for all detectable problems
                .penaltyLog()
                .build()
        )
        super.onCreate()
        // initialize sdk
        FcmClientHelper.getInstance(applicationContext).initialise(Logger.LogLevel.VERBOSE, 4)
        FcmClientHelper.getInstance(applicationContext).addListener(FcmListener1())
        FcmClientHelper.getInstance(applicationContext).addListener(FcmListener2())
    }
}
