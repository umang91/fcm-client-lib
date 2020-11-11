package com.umang.fcmclient

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.umang.fcmclient.util.Logger

/**
 * Observer for activity lifecycle
 * @author Umang Chamaria
 */
public class ActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

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
        try {
            logger.verbose(" onActivityStarted() Activity Started ${activity::class.qualifiedName}")
            FcmClientHelper.getInstance(activity.applicationContext).onStart()
        } catch (e: Exception) {
            logger.error(" onActivityStarted() ", e)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        try {
            logger.verbose(" onActivityStopped() Activity Stopped ${activity::class.qualifiedName}")
            FcmClientHelper.getInstance(activity.applicationContext).onStop()
        } catch (e: Exception) {
            logger.error("onActivityStopped() ", e)
        }
    }
}