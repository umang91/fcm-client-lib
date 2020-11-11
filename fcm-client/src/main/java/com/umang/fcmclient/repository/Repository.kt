package com.umang.fcmclient.repository

import android.content.Context

/**
 * @author Umang Chamaria
 * Date: 28/04/20
 */
internal class Repository(private val context: Context) {

    private val sharedPref = SharedPref(context)

    internal fun saveToken(token: String) {
        sharedPref.putString(PREF_KEY_PUSH_TOKEN, token)
    }

    internal fun getToken(): String {
        return sharedPref.getString(PREF_KEY_PUSH_TOKEN, "") as String
    }
}

private const val PREF_KEY_PUSH_TOKEN = "push_token"