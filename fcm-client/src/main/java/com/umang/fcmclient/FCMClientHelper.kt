package com.umang.fcmclient

import android.content.Context

/**
 * @author Umang Chamaria
 */
class FCMClientHelper private constructor(var context: Context){

  companion object {
    fun newInstance(context: Context): FCMClientHelper{
      return FCMClientHelper(context)
    }
  }
}