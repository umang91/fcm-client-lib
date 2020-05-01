package com.umang.example.fcmclient

import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import com.umang.fcmclient.listeners.FirebaseMessageListener

/**
 * @author Umang Chamaria
 * Date: 30/04/20
 */
class FcmListener2 : FirebaseMessageListener {
    override fun onTokenAvailable(token: String) {
        Log.v("FcmListener2", "Token: $token")
    }

    override fun onPushReceived(remoteMessage: RemoteMessage) {
        Log.v("FcmListener2", "Push Message: $remoteMessage")
    }
}