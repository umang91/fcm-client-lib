package com.umang.fcmclient

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle

/**
 * @author Umang Chamaria
 */
class FCMClientLibActivityLifecycleCallbacks:Application.ActivityLifecycleCallbacks {
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

  override fun onActivityStarted(activity: Activity?) {
    val context: Context? = activity?.applicationContext
    if (context != null) {
      FCMClientHelper.newInstance(context).onStart()
    }
  }

  override fun onActivityStopped(activity: Activity?) {
    val context: Context? = activity?.applicationContext
    if (context != null) {
      FCMClientHelper.newInstance(context).onStop()
    }
  }
}