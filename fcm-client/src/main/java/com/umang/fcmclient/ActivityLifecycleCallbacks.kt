package com.umang.fcmclient

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * @author Umang Chamaria
 */
class ActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
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
    FcmClientHelper.getInstance(activity.applicationContext).onStart()
  }

  override fun onActivityStopped(activity: Activity) {
    FcmClientHelper.getInstance(activity.applicationContext).onStop()
  }
}