package com.umang.fcmclient

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.umang.fcmclient.util.Logger

/**
 * @author Umang Chamaria
 */
class ActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    private val logger = Logger.getLogger("ActivityLifecycleCallbacks")

    override fun onActivityPaused(p0: Activity?) {
    }

    override fun onActivityResumed(p0: Activity?) {
    }

    override fun onActivityDestroyed(p0: Activity?) {
    }

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
    }

    override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        logger.verbose(" onActivityStarted() Activity Started ${activity::class.qualifiedName}")
        FcmClientHelper.getInstance(activity.applicationContext).onStart()
    }

    override fun onActivityStopped(activity: Activity) {
        logger.verbose(" onActivityStopped() Activity Stopped ${activity::class.qualifiedName}")
        FcmClientHelper.getInstance(activity.applicationContext).onStop()
    }
}