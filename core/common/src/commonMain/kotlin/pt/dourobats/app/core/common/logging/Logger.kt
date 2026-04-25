package pt.dourobats.app.core.common.logging

/**
 * Domain port for structured logging across all platforms.
 *
 * All methods accept an optional [tag] parameter. When null, the platform adapter
 * falls back to a sensible default. Callers that want explicit tagging can pass
 * their class name (or a companion `TAG` constant), but omitting it is valid.
 *
 * **Security:** never pass sensitive data (OTP codes, emails, auth tokens) to any
 * log method — even debug logs can be captured by an attached debugger.
 */
interface Logger {
    fun d(message: String, tag: String? = null)
    fun i(message: String, tag: String? = null)
    fun w(message: String, tag: String? = null)
    fun e(message: String, throwable: Throwable? = null, tag: String? = null)
}
