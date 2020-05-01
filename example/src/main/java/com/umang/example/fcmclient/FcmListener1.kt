package com.umang.example.fcmclient

import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import com.umang.fcmclient.listeners.FirebaseMessageListener

/**
 * @author Umang Chamaria
 * Date: 28/04/20
 */
class FcmListener1: FirebaseMessageListener {
    override fun onTokenAvailable(token: String) {
        Log.v("FcmListener1", "Token: $token")
    }

    override fun onPushReceived(remoteMessage: RemoteMessage) {
        Log.v("FcmListener1", "Push Message: $remoteMessage")
    }
}