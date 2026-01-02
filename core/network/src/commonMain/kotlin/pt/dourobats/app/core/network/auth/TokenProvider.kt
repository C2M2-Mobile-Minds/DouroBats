package pt.dourobats.app.core.network.auth

/**
 * Provides authentication tokens for API requests.
 * Implementation will be provided by the auth module.
 */
interface TokenProvider {
    /**
     * Gets the current auth token, or null if not authenticated.
     */
    suspend fun getToken(): String?

    /**
     * Clears the stored token (e.g., on logout).
     */
    suspend fun clearToken()
}

/**
 * Default implementation that returns no token.
 * Used in Phase 1 before authentication is implemented.
 */
class NoOpTokenProvider : TokenProvider {
    override suspend fun getToken(): String? = null
    override suspend fun clearToken() {}
}
