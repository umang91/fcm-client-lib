package com.umang.fcmclient

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * @author Umang Chamaria
 */
class FCMClientLibMessageService: FirebaseMessagingService() {
  override fun onMessageReceived(p0: RemoteMessage?) {
    super.onMessageReceived(p0)
  }
}