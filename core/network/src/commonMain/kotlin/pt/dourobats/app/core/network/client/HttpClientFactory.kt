package pt.dourobats.app.core.network.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import pt.dourobats.app.core.network.auth.TokenProvider

/**
 * Factory for creating configured HttpClient instances.
 * Expects platform-specific engine to be provided.
 */
object HttpClientFactory {

    /**
     * Creates a configured HttpClient with all required plugins.
     *
     * @param engine Platform-specific HTTP engine
     * @param tokenProvider Provider for auth tokens
     * @return Configured HttpClient
     */
    fun create(
        engine: HttpClientEngine,
        tokenProvider: TokenProvider
    ): HttpClient {
        return HttpClient(engine) {
            // Configure all plugins
            configureJsonSerialization()
            configureTimeout()
            configureLogging()
            configureAuth(tokenProvider)
            configureDefaults()

            // Ensure exceptions are exposed for proper error handling
            expectSuccess = true
        }
    }
}
