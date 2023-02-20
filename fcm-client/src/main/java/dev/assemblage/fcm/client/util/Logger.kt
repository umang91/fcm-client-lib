package dev.assemblage.fcm.client.util

import android.util.Log
import dev.assemblage.fcm.client.BuildConfig

/**
 * @author Umang Chamaria
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

    public fun log(
        level: LogLevel = LogLevel.VERBOSE,
        throwable: Throwable? = null,
        message: () -> String
    ) {
        try {
            if (level < logLevel) return
            when (level) {
                LogLevel.VERBOSE -> {
                    Log.v(tag, "$className ${message()}")
                }
                LogLevel.INFO -> {
                    Log.i(tag, "$className ${message()}")
                }
                LogLevel.ERROR -> {
                    Log.e(tag, "$className ${message()}", throwable)
                }
                LogLevel.WARN -> {
                    Log.w(tag, "$className ${message()}")
                }
                LogLevel.DEBUG -> {
                    Log.d(tag, "$className ${message()}")
                }
                LogLevel.NONE -> {
                }
            }
        } catch (e: Exception) {
        }
    }

    public companion object {
        internal fun getLogger(className: String) = Logger(className)

        internal var logLevel: LogLevel = LogLevel.ERROR

        internal var isLogEnabled = false

        private const val tag = "FcmClient_v${BuildConfig.FCM_CLIENT_VERSION}"
    }
}
