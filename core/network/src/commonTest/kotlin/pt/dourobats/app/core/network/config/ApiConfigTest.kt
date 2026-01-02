package pt.dourobats.app.core.network.config

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

class ApiConfigTest {

    @Test
    fun `timeout values are correct when accessed`() {
        // When
        val requestTimeout = ApiConfig.requestTimeout
        val connectTimeout = ApiConfig.connectTimeout
        val socketTimeout = ApiConfig.socketTimeout

        // Then
        assertEquals(30.seconds, requestTimeout, "Request timeout should be 30 seconds")
        assertEquals(15.seconds, connectTimeout, "Connect timeout should be 15 seconds")
        assertEquals(30.seconds, socketTimeout, "Socket timeout should be 30 seconds")
    }

    @Test
    fun `timeout values are positive when accessed`() {
        // When/Then
        assertTrue(
            ApiConfig.requestTimeout.isPositive(),
            "Request timeout should be positive"
        )
        assertTrue(
            ApiConfig.connectTimeout.isPositive(),
            "Connect timeout should be positive"
        )
        assertTrue(
            ApiConfig.socketTimeout.isPositive(),
            "Socket timeout should be positive"
        )
    }

    @Test
    fun `baseUrl delegates to Environment when accessed`() {
        // When
        val baseUrl = ApiConfig.baseUrl
        val environmentBaseUrl = ApiConfig.environment.baseUrl

        // Then
        assertEquals(
            environmentBaseUrl,
            baseUrl,
            "ApiConfig.baseUrl should delegate to Environment.baseUrl"
        )
    }

    @Test
    fun `enableLogging delegates to Environment when accessed`() {
        // When
        val enableLogging = ApiConfig.enableLogging
        val environmentLogging = ApiConfig.environment.enableLogging

        // Then
        assertEquals(
            environmentLogging,
            enableLogging,
            "ApiConfig.enableLogging should delegate to Environment.enableLogging"
        )
    }

    @Test
    fun `environment matches current Environment when accessed`() {
        // When
        val apiConfigEnv = ApiConfig.environment
        val currentEnv = Environment.current

        // Then
        assertEquals(
            currentEnv,
            apiConfigEnv,
            "ApiConfig.environment should match Environment.current"
        )
    }

    @Test
    fun `connectTimeout is less than requestTimeout when compared`() {
        // When
        val connectTimeout = ApiConfig.connectTimeout
        val requestTimeout = ApiConfig.requestTimeout

        // Then
        assertTrue(
            connectTimeout < requestTimeout,
            "Connect timeout ($connectTimeout) should be less than request timeout ($requestTimeout)"
        )
    }
}
