package com.umang.fcmclient

import com.google.firebase.iid.FirebaseInstanceIdService
import com.umang.logger.SmartLogger
import com.umang.logger.verbose

/**
 * @author Umang Chamaria
 */
class FCMClientLibInstanceIDService : FirebaseInstanceIdService(), SmartLogger {
  override fun onTokenRefresh() {
    super.onTokenRefresh()
    verbose("onTokenRefresh(): token refreshed will register for push token")
    FCMClientHelper.newInstance(applicationContext).refreshToken()
  }
}