package pt.dourobats.app.core.network.config

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EnvironmentTest {

    @Test
    fun `DEVELOPMENT has correct configuration when accessed`() {
        // Given
        val env = Environment.DEVELOPMENT

        // Then
        assertEquals("http://localhost:8080", env.baseUrl)
        assertTrue(env.enableLogging, "DEVELOPMENT should have logging enabled")
    }

    @Test
    fun `STAGING has correct configuration when accessed`() {
        // Given
        val env = Environment.STAGING

        // Then
        assertEquals("https://staging-api.dourobats.pt", env.baseUrl)
        assertTrue(env.enableLogging, "STAGING should have logging enabled")
    }

    @Test
    fun `PRODUCTION has correct configuration when accessed`() {
        // Given
        val env = Environment.PRODUCTION

        // Then
        assertEquals("https://api.dourobats.pt", env.baseUrl)
        assertFalse(env.enableLogging, "PRODUCTION should have logging disabled")
    }

    @Test
    fun `current is DEVELOPMENT when no environment configured`() {
        // When
        val current = Environment.current

        // Then
        assertEquals(Environment.DEVELOPMENT, current)
    }

    @Test
    fun `baseUrl is non-empty when environment accessed`() {
        // When/Then
        Environment.values().forEach { env ->
            assertTrue(
                env.baseUrl.isNotEmpty(),
                "Environment ${env.name} should have non-empty baseUrl"
            )
        }
    }

    @Test
    fun `baseUrl is valid HTTP URL when environment accessed`() {
        // When/Then
        Environment.values().forEach { env ->
            assertTrue(
                env.baseUrl.startsWith("http://") || env.baseUrl.startsWith("https://"),
                "Environment ${env.name} should have HTTP or HTTPS URL, got: ${env.baseUrl}"
            )
        }
    }

    @Test
    fun `enableLogging is false only for PRODUCTION when environments compared`() {
        // When/Then
        assertEquals(false, Environment.PRODUCTION.enableLogging)
        assertEquals(true, Environment.DEVELOPMENT.enableLogging)
        assertEquals(true, Environment.STAGING.enableLogging)
    }
}
