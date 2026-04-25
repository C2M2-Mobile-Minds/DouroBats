package pt.dourobats.app.core.testing.logging

import pt.dourobats.app.core.common.logging.Logger

/**
 * Property-based fake [Logger] for unit tests.
 *
 * Stores all log calls in typed lists so tests can assert that specific messages
 * were logged without coupling to println output.
 *
 * Usage:
 * ```kotlin
 * val logger = FakeLogger()
 * val sut = MyClass(logger)
 * sut.doSomething()
 * assertTrue(logger.warnings.any { it.contains("venue") })
 * ```
 */
class FakeLogger : Logger {
    val debugs: MutableList<String> = mutableListOf()
    val infos: MutableList<String> = mutableListOf()
    val warnings: MutableList<String> = mutableListOf()
    val errors: MutableList<String> = mutableListOf()

    override fun d(message: String, tag: String?) {
        debugs.add("[$tag] $message")
    }

    override fun i(message: String, tag: String?) {
        infos.add("[$tag] $message")
    }

    override fun w(message: String, tag: String?) {
        warnings.add("[$tag] $message")
    }

    override fun e(message: String, throwable: Throwable?, tag: String?) {
        errors.add("[$tag] $message${throwable?.let { ": $it" } ?: ""}")
    }

    fun clear() {
        debugs.clear()
        infos.clear()
        warnings.clear()
        errors.clear()
    }
}
