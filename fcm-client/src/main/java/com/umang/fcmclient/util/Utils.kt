package com.umang.fcmclient.util

import android.content.Context
import android.content.pm.ApplicationInfo




/**
 * @author Umang Chamaria
 * Date: 01/05/20
 */

fun isDebugBuild(context: Context): Boolean{
    return 0 != context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
}