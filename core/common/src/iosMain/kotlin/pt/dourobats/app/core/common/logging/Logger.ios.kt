package pt.dourobats.app.core.common.logging

import platform.Foundation.NSLog
import pt.dourobats.app.core.common.isDebug

private const val DEFAULT_TAG = "DouroBats"

actual fun createPlatformLogger(): Logger = object : Logger {
    // Debug logs suppressed in release builds to avoid leaking sensitive info.
    override fun d(message: String, tag: String?) {
        if (isDebug) NSLog("[DEBUG][${tag ?: DEFAULT_TAG}] $message")
    }

    override fun i(message: String, tag: String?) {
        NSLog("[INFO][${tag ?: DEFAULT_TAG}] $message")
    }

    override fun w(message: String, tag: String?) {
        NSLog("[WARN][${tag ?: DEFAULT_TAG}] $message")
    }

    override fun e(message: String, throwable: Throwable?, tag: String?) {
        NSLog("[ERROR][${tag ?: DEFAULT_TAG}] $message${throwable?.let { ": $it" } ?: ""}")
    }
}
