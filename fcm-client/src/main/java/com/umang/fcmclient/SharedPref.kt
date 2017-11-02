package com.umang.fcmclient

import android.content.Context
import android.content.SharedPreferences

/**
 * @author Umang Chamaria
 */
class SharedPref private constructor(private val context: Context){

  private val fileName = "umang_fcm_client_lib"
  private val pref: SharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

  private val prefKeyPushToken = "push_token"

  var pushToken: String?
    get() = pref.getString(prefKeyPushToken, null)
    set(value) = pref.edit().putString(prefKeyPushToken, value).apply()

  companion object {
    fun newInstance(context: Context): SharedPref {
      return SharedPref(context)
    }
  }
}