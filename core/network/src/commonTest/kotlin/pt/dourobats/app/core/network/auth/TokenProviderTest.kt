package pt.dourobats.app.core.network.auth

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TokenProviderTest {

    @Test
    fun `getToken returns null when NoOpTokenProvider used`() = runTest {
        // Given
        val tokenProvider = NoOpTokenProvider()

        // When
        val token = tokenProvider.getToken()

        // Then
        assertNull(token, "NoOpTokenProvider should return null token")
    }

    @Test
    fun `clearToken completes successfully when NoOpTokenProvider used`() = runTest {
        // Given
        val tokenProvider = NoOpTokenProvider()

        // When - Should not throw exception
        tokenProvider.clearToken()

        // Then - Token should still be null
        assertNull(tokenProvider.getToken())
    }

    @Test
    fun `getToken returns null consistently when NoOpTokenProvider called multiple times`() = runTest {
        // Given
        val tokenProvider = NoOpTokenProvider()

        // When
        val token1 = tokenProvider.getToken()
        val token2 = tokenProvider.getToken()
        tokenProvider.clearToken()
        val token3 = tokenProvider.getToken()

        // Then
        assertNull(token1)
        assertNull(token2)
        assertNull(token3)
    }

    @Test
    fun `getToken returns token when custom TokenProvider provides token`() = runTest {
        // Given
        val testToken = "test-token-12345"
        val tokenProvider = TestTokenProvider(testToken)

        // When
        val token = tokenProvider.getToken()

        // Then
        assertEquals(testToken, token)
        assertTrue(tokenProvider.wasGetTokenCalled)
    }

    @Test
    fun `clearToken is called when custom TokenProvider clears token`() = runTest {
        // Given
        val testToken = "test-token-12345"
        val tokenProvider = TestTokenProvider(testToken)

        // When
        tokenProvider.clearToken()

        // Then
        assertTrue(tokenProvider.wasClearTokenCalled)
    }

    // Helper class for testing TokenProvider interface
    private class TestTokenProvider(private val token: String) : TokenProvider {
        var wasGetTokenCalled = false
        var wasClearTokenCalled = false

        override suspend fun getToken(): String {
            wasGetTokenCalled = true
            return token
        }

        override suspend fun clearToken() {
            wasClearTokenCalled = true
        }
    }

    private fun assertEquals(expected: String, actual: String?) {
        kotlin.test.assertEquals(expected, actual)
    }
}
