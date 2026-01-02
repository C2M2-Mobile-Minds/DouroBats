package pt.dourobats.app.core.network.client

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import pt.dourobats.app.core.network.auth.TokenProvider
import pt.dourobats.app.core.network.config.ApiConfig

/**
 * Configures JSON content negotiation with kotlinx.serialization
 */
fun HttpClientConfig<*>.configureJsonSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true  // Resilient to API changes
            encodeDefaults = true
        })
    }
}

/**
 * Configures HTTP timeouts
 */
fun HttpClientConfig<*>.configureTimeout() {
    install(HttpTimeout) {
        requestTimeoutMillis = ApiConfig.requestTimeout.inWholeMilliseconds
        connectTimeoutMillis = ApiConfig.connectTimeout.inWholeMilliseconds
        socketTimeoutMillis = ApiConfig.socketTimeout.inWholeMilliseconds
    }
}

/**
 * Configures logging for debugging
 */
fun HttpClientConfig<*>.configureLogging() {
    if (ApiConfig.enableLogging) {
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL  // Log headers, body, info for debugging
        }
    }
}

/**
 * Configures Bearer token authentication
 */
fun HttpClientConfig<*>.configureAuth(tokenProvider: TokenProvider) {
    install(Auth) {
        bearer {
            loadTokens {
                // Load token from provider
                val token = tokenProvider.getToken()
                token?.let {
                    BearerTokens(accessToken = it, refreshToken = "")
                }
            }

            // Optional: Refresh token logic (Phase 2)
            // refreshTokens {
            //     val newToken = authRepository.refreshToken()
            //     BearerTokens(accessToken = newToken, refreshToken = "")
            // }
        }
    }
}

/**
 * Configures default request settings
 */
fun HttpClientConfig<*>.configureDefaults() {
    defaultRequest {
        url(ApiConfig.baseUrl)
        header(HttpHeaders.ContentType, ContentType.Application.Json)
    }
}
