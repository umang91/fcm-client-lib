package com.umang.fcmclient.repository

import android.content.Context
import android.content.SharedPreferences
import com.umang.fcmclient.SHARED_PREFERENCE_FILE_NAME

/**
 * Helper class to save strings in [SharedPreferences]
 * @author Umang Chamaria
 */
class SharedPref(private val context: Context) {

    private val pref = context.getSharedPreferences(SHARED_PREFERENCE_FILE_NAME, Context
            .MODE_PRIVATE)

    fun putString(key: String, value: String?) {
        pref.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String): String? {
        return pref.getString(key, defaultValue)
    }
}