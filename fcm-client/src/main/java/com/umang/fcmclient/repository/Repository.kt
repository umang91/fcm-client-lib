package com.umang.fcmclient.repository

import android.content.Context
import com.umang.fcmclient.AsyncExecutor

/**
 *
 * @author Umang Chamaria
 * Date: 28/04/20
 */
private const val PREF_KEY_PUSH_TOKEN = "push_token"

internal class Repository(private val context: Context) {

    private val sharedPref = SharedPref(context)

    internal fun saveToken(token: String) {
        AsyncExecutor.submit {
            sharedPref.putString(PREF_KEY_PUSH_TOKEN, token)
        }
    }

    internal fun getToken(): String {
        return sharedPref.getString(PREF_KEY_PUSH_TOKEN, "") as String
    }
}