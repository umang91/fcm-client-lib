package com.umang.fcmclient

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.umang.fcmclient.util.Logger

/**
 * @author Umang Chamaria
 */
class FcmClientMessageService : FirebaseMessagingService() {

    private val logger = Logger.getLogger("FcmClientMessageService")

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        logger.verbose(" onMessageReceived() Firebase message received.")
        FcmClientHelper.getInstance(applicationContext).onPushReceived(remoteMessage)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        logger.verbose("onNewToken(): $token")
        FcmClientHelper.getInstance(applicationContext).onNewToken(token)
    }
}