package com.umang.fcmclient.util

import android.content.Context
import android.content.pm.ApplicationInfo


/**
 * @author Umang Chamaria
 * Date: 01/05/20
 */

/**
 * Checks if the application is debuggable or not.
 * @param context instance of [Context]
 *
 * @return true is the application is debuggable else false.
 */
internal fun isDebugBuild(context: Context): Boolean {
    return 0 != context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
}