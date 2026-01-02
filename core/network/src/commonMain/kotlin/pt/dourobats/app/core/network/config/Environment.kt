package pt.dourobats.app.core.network.config

/**
 * Represents different deployment environments.
 */
enum class Environment(
    val baseUrl: String,
    val enableLogging: Boolean
) {
    /**
     * Development environment - local backend or dev server
     */
    DEVELOPMENT(
        baseUrl = "http://localhost:8080",
        enableLogging = true
    ),

    /**
     * Staging environment - testing before production
     */
    STAGING(
        baseUrl = "https://staging-api.dourobats.pt",
        enableLogging = true
    ),

    /**
     * Production environment - live API
     */
    PRODUCTION(
        baseUrl = "https://api.dourobats.pt",
        enableLogging = false
    );

    companion object {
        /**
         * Current active environment.
         * In Phase 1 (local-only), this defaults to DEVELOPMENT.
         * In Phase 2, this can be changed via build variants or feature flags.
         */
        val current: Environment = DEVELOPMENT
    }
}
