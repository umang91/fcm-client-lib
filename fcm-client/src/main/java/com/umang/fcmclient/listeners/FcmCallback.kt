package com.umang.fcmclient.listeners

import com.google.firebase.messaging.RemoteMessage

/**
 * Interface definition for Firebase Cloud Messaging(FCM) Callbacks
 *
 * @author Umang Chamaria
 * Date: 27/04/20
 */
interface FcmCallback {
    /**
     * Callback for new token generated.
     * @param token Token generated received from FCM callback.
     */
    fun onTokenAvailable(token: String)

    /**
     * Callback for Push Message received from FCM
     * @param remoteMessage instance of [RemoteMessage] received from firebase.
     */
    fun onPushReceived(remoteMessage: RemoteMessage)
}