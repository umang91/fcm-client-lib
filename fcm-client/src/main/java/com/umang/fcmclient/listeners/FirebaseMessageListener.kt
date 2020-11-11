package com.umang.fcmclient.listeners

import com.google.firebase.messaging.RemoteMessage

/**
 * Interface definition for Firebase Cloud Messaging(FCM) Callbacks
 *
 * @author Umang Chamaria
 * Date: 27/04/20
 */
public interface FirebaseMessageListener {
    /**
     * Callback for new token generated.
     * @param token Token generated received from FCM callback.
     */
    public fun onTokenAvailable(token: String)

    /**
     * Callback for Push Message received from FCM
     * @param remoteMessage instance of [RemoteMessage] received from firebase.
     */
    public fun onPushReceived(remoteMessage: RemoteMessage)
}