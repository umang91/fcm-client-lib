package com.umang.fcmclient.util

import android.util.Log
import com.umang.fcmclient.BuildConfig

/**
 * @author Umang Chamaria
 * Date: 28/04/20
 */
public class Logger private constructor(private val className: String) {

    /**
     * Logging level
     */
    public enum class LogLevel {
        /**
         * Priority constant for printing no logs.
         */
        NONE,

        /**
         * Priority constant for printing info logs.
         */
        INFO,

        /**
         * Priority constant for printing error logs.
         */
        ERROR,

        /**
         * Priority constant for printing warning logs.
         */
        WARN,

        /**
         * Priority constant for printing debug logs.
         */
        DEBUG,

        /**
         * Priority constant for printing verbose logs.
         */
        VERBOSE
    }


    /**
     *
     */
    public fun verbose(message: String) {
        if (logLevel >= LogLevel.VERBOSE && isLogEnabled) {
            Log.v(tag, "$className $message")
        }
    }

    public fun debug(message: String) {
        if (logLevel >= LogLevel.DEBUG && isLogEnabled) {
            Log.d(tag, "$className $message")
        }
    }

    public fun info(message: String) {
        if (logLevel >= LogLevel.INFO && isLogEnabled) {
            Log.i(tag, "$className $message")
        }
    }

    public fun error(message: String, throwable: Throwable? = null) {
        if (logLevel >= LogLevel.ERROR && isLogEnabled) {
            if (throwable != null) Log.e(tag, "$className $message", throwable)
            else Log.e(tag, "$className $message")
        }
    }

    public fun warn(message: String) {
        if (logLevel >= LogLevel.WARN && isLogEnabled) {
            Log.w(tag, "$className $message")
        }
    }

    public companion object {
        internal fun getLogger(className: String) = Logger(className)

        internal var logLevel: LogLevel = LogLevel.ERROR

        internal var isLogEnabled = false

        private const val tag = "FcmClient_v${BuildConfig.FCM_CLIENT_VERSION}"
    }
}