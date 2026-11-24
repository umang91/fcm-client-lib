package dev.assemblage.fcm.client

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.assemblage.fcm.client.internal.util.Logger

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

    override fun onRegistered(installationId: String) {
        super.onRegistered(installationId)
        logger.log { "onRegistered(): $installationId" }
        FcmClientHelper.getInstance(applicationContext).onRegistered(installationId)
    }
}
