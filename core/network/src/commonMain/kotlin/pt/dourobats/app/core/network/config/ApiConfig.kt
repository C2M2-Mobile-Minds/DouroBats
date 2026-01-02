package pt.dourobats.app.core.network.config

import kotlin.time.Duration.Companion.seconds

/**
 * API configuration for HTTP client.
 * Provides timeouts and environment settings.
 */
object ApiConfig {
    /**
     * Current environment configuration
     */
    val environment: Environment = Environment.current

    /**
     * Base URL for API requests
     */
    val baseUrl: String
        get() = environment.baseUrl

    /**
     * Request timeout - maximum time to wait for a response
     */
    val requestTimeout = 30.seconds

    /**
     * Connect timeout - maximum time to establish a connection
     */
    val connectTimeout = 15.seconds

    /**
     * Socket timeout - maximum time between two data packets
     */
    val socketTimeout = 30.seconds

    /**
     * Whether to enable detailed logging
     */
    val enableLogging: Boolean
        get() = environment.enableLogging
}
