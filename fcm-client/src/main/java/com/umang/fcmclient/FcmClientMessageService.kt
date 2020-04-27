package com.umang.fcmclient

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.umang.logger.SmartLogger
import com.umang.logger.verbose

/**
 * @author Umang Chamaria
 */
class FcmClientMessageService : FirebaseMessagingService(), SmartLogger {

  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    super.onMessageReceived(remoteMessage)
    FcmClientHelper.getInstance(applicationContext).onPushReceived(remoteMessage)
  }

  override fun onNewToken(token: String) {
    super.onNewToken(token)
    verbose("onNewToken(): $token")
    FcmClientHelper.getInstance(applicationContext).onNewToken(token)
  }
}