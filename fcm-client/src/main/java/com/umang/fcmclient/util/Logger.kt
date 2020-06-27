package com.umang.fcmclient.util

import android.util.Log
import com.umang.fcmclient.BuildConfig

/**
 * @author Umang Chamaria
 * Date: 28/04/20
 */
class Logger private constructor(private val className: String) {

    /**
     * Logging level
     */
    enum class LogLevel {
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
    fun verbose(message: String) {
        if (logLevel >= LogLevel.VERBOSE && isLogEnabled) {
            Log.v(tag, "$className $message")
        }
    }

    fun debug(message: String) {
        if (logLevel >= LogLevel.DEBUG && isLogEnabled) {
            Log.d(tag, "$className $message")
        }
    }

    fun info(message: String) {
        if (logLevel >= LogLevel.INFO && isLogEnabled) {
            Log.i(tag, "$className $message")
        }
    }

    fun error(message: String, throwable: Throwable? = null) {
        if (logLevel >= LogLevel.ERROR && isLogEnabled) {
            if (throwable != null) Log.e(tag, "$className $message", throwable)
            else Log.e(tag, "$className $message")
        }
    }

    fun warn(message: String) {
        if (logLevel >= LogLevel.WARN && isLogEnabled) {
            Log.w(tag, "$className $message")
        }
    }

    companion object {
        fun getLogger(className: String) = Logger(className)

        internal var logLevel: LogLevel = LogLevel.ERROR

        internal var isLogEnabled = false

        private const val tag = "FcmClient_v${BuildConfig.FCM_CLIENT_VERSION}"
    }
}