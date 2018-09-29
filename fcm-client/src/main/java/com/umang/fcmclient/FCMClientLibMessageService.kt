package com.umang.fcmclient

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.umang.logger.SmartLogger
import com.umang.logger.verbose

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

  override fun onNewToken(token: String) {
    super.onNewToken(token)
    verbose("onNewToken(): $token")
    FCMClientHelper.newInstance(applicationContext).onNewToken(token)
  }
}