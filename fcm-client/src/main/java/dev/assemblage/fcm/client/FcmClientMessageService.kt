package dev.assemblage.fcm.client

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.assemblage.fcm.client.util.Logger

/**
 * Class to handle callbacks from Firebase Messaging Library.
 *
 * @author Umang Chamaria
 */
public class FcmClientMessageService : FirebaseMessagingService() {

    private val logger = Logger.getLogger("FcmClientMessageService")

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        logger.log { " onMessageReceived() Firebase message received." }
        FcmClientHelper.getInstance(applicationContext).onPushReceived(remoteMessage)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        logger.log { " onNewToken(): $token" }
        FcmClientHelper.getInstance(applicationContext).onNewToken(token)
    }
}