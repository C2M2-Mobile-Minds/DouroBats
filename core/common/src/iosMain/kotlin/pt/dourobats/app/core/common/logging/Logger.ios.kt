package pt.dourobats.app.core.common.logging

actual fun createPlatformLogger(): Logger = object : Logger {
    override fun debug(tag: String, message: String) = println("[DEBUG][$tag] $message")
    override fun info(tag: String, message: String) = println("[INFO][$tag] $message")
    override fun warn(tag: String, message: String) = println("[WARN][$tag] $message")
    override fun error(tag: String, message: String, throwable: Throwable?) =
        println("[ERROR][$tag] $message${throwable?.let { ": $it" } ?: ""}")
}
