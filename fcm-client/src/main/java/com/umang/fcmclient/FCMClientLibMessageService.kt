package com.umang.fcmclient

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.umang.logger.SmartLogger

/**
 * @author Umang Chamaria
 */
class FCMClientLibMessageService: FirebaseMessagingService(),SmartLogger{
  override fun onMessageReceived(p0: RemoteMessage?) {
    super.onMessageReceived(p0)
    if (p0 != null) {
      FCMClientHelper.newInstance(applicationContext).onPushReceived(p0)
    }
  }
}