package com.umang.fcmclient

import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * @author Umang Chamaria
 */
class FCMClientLibInstanceIDService : FirebaseInstanceIdService() {
  override fun onTokenRefresh() {
    super.onTokenRefresh()
  }
}