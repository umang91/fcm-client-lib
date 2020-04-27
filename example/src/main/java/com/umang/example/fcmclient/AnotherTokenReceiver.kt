package com.umang.example.fcmclient

import android.util.Log
import com.umang.fcmclient.FcmClientHelper

/**
 * @author Umang Chamaria
 */
class AnotherTokenReceiver: FcmClientHelper.TokenReceivedListener {
  override fun onTokenReceived(token: String) {
    Log.v("SampleApplication", "AnotherTokenReceiver : $token")
  }
}