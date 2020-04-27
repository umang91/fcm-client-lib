package com.umang.example.fcmclient

import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import com.umang.fcmclient.FcmClientHelper

/**
 * @author Umang Chamaria
 */
class PushReceiver: FcmClientHelper.PushReceivedListener {
  override fun onPushReceived(remoteMessage: RemoteMessage) {
    for ((key, value) in remoteMessage.data){
      Log.v("SampleApplication", "Key: $key, Value: $value")
    }
    //display push or pass to other sdks if required
  }
}