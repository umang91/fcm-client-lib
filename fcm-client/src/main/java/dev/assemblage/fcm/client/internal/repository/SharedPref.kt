package dev.assemblage.fcm.client.internal.repository

import android.content.Context
import android.content.SharedPreferences
import dev.assemblage.fcm.client.internal.SHARED_PREFERENCE_FILE_NAME

/**
 * Helper class to save strings in [SharedPreferences]
 *
 * @author Umang Chamaria
 */
internal class SharedPref(context: Context) {

    private val pref =
        context.getSharedPreferences(SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)

    internal fun putString(key: String, value: String?) {
        pref.edit().putString(key, value).apply()
    }

    internal fun getString(key: String, defaultValue: String): String? {
        return pref.getString(key, defaultValue)
    }
}
