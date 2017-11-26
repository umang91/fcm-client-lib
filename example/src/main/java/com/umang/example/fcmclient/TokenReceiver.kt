package com.umang.example.fcmclient

import android.util.Log
import com.umang.fcmclient.FCMClientHelper

/**
 * @author Umang Chamaria
 */
class TokenReceiver:FCMClientHelper.TokenReceivedListener {
  override fun onTokenReceived(token: String) {
    Log.v("SampleApplication", "Token: $token")
  }
}