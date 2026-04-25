package pt.dourobats.app.core.common.logging

import android.util.Log
import pt.dourobats.app.core.common.isDebug

private const val DEFAULT_TAG = "DouroBats"

actual fun createPlatformLogger(): Logger = object : Logger {
    // Debug logs suppressed in release builds to avoid leaking sensitive info.
    override fun d(message: String, tag: String?) {
        if (isDebug) Log.d(tag ?: DEFAULT_TAG, message)
    }

    override fun i(message: String, tag: String?) {
        Log.i(tag ?: DEFAULT_TAG, message)
    }

    override fun w(message: String, tag: String?) {
        Log.w(tag ?: DEFAULT_TAG, message)
    }

    override fun e(message: String, throwable: Throwable?, tag: String?) {
        Log.e(tag ?: DEFAULT_TAG, message, throwable)
    }
}
