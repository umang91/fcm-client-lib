package com.umang.fcmclient.repository

import android.content.Context

/**
 * @author Umang Chamaria
 * Date: 28/04/20
 */
class Repository(private val context: Context) {

    private val sharedPref = SharedPref(context)

    fun saveToken(token: String) {
        sharedPref.putString(PREF_KEY_PUSH_TOKEN, token)
    }

    fun getToken(): String {
        return sharedPref.getString(PREF_KEY_PUSH_TOKEN, "") as String
    }
}

const val PREF_KEY_PUSH_TOKEN = "push_token"