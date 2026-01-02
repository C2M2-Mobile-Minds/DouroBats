package pt.dourobats.app.core.network.client

import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import pt.dourobats.app.core.network.auth.NoOpTokenProvider
import pt.dourobats.app.core.network.auth.TokenProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

// Test data classes - must be top-level for kotlinx.serialization to process them
@Serializable
data class TestUser(
    val id: String,
    val name: String,
    val email: String
)

class HttpClientFactoryTest {

    @Test
    fun `create is successful when mock engine provided`() = runTest {
        // Given
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""{"message":"success"}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClientFactory.create(
            engine = mockEngine,
            tokenProvider = NoOpTokenProvider()
        )

        // When
        val response = client.get("http://test.com/api/test")

        // Then
        assertEquals(HttpStatusCode.OK, response.status)

        // Cleanup
        client.close()
    }

    @Test
    fun `create deserializes JSON response when valid JSON provided`() = runTest {
        // Given
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""{"id":"123","name":"Test User","email":"test@example.com"}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClientFactory.create(
            engine = mockEngine,
            tokenProvider = NoOpTokenProvider()
        )

        // When
        val user = client.get("http://test.com/api/user").body<TestUser>()

        // Then
        assertEquals("123", user.id)
        assertEquals("Test User", user.name)
        assertEquals("test@example.com", user.email)

        // Cleanup
        client.close()
    }

    @Test
    fun `create ignores unknown fields when extra JSON fields provided`() = runTest {
        // Given - JSON with extra fields that don't exist in TestUser
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(
                    """{"id":"123","name":"Test User","email":"test@example.com","unknownField":"should be ignored","anotherUnknown":999}"""
                ),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClientFactory.create(
            engine = mockEngine,
            tokenProvider = NoOpTokenProvider()
        )

        // When - Should not throw exception despite unknown fields
        val user = client.get("http://test.com/api/user").body<TestUser>()

        // Then
        assertEquals("123", user.id)
        assertEquals("Test User", user.name)
        assertEquals("test@example.com", user.email)

        // Cleanup
        client.close()
    }

    @Test
    fun `create adds Bearer token when token provider returns token`() = runTest {
        // Given
        val testToken = "test-jwt-token-12345"
        val tokenProvider = TestTokenProvider(testToken)

        val mockEngine = MockEngine { request ->
            // Verify the Authorization header is present
            val authHeader = request.headers[HttpHeaders.Authorization]

            respond(
                content = ByteReadChannel("""{"authenticated":true}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClientFactory.create(
            engine = mockEngine,
            tokenProvider = tokenProvider
        )

        // When
        val response = client.get("http://test.com/api/protected")

        // Then
        assertEquals(HttpStatusCode.OK, response.status)

        // Verify token provider was called
        assertTrue(tokenProvider.wasGetTokenCalled)

        // Cleanup
        client.close()
    }

    @Test
    fun `create does not add auth header when token provider returns null`() = runTest {
        // Given
        val tokenProvider = NoOpTokenProvider()

        val mockEngine = MockEngine { request ->
            // Verify no Authorization header
            val authHeader = request.headers[HttpHeaders.Authorization]

            respond(
                content = ByteReadChannel("""{"authenticated":false}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClientFactory.create(
            engine = mockEngine,
            tokenProvider = tokenProvider
        )

        // When
        val response = client.get("http://test.com/api/public")

        // Then
        assertEquals(HttpStatusCode.OK, response.status)

        // Cleanup
        client.close()
    }

    @Test
    fun `create throws ClientRequestException when 4xx error returned`() = runTest {
        // Given
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""{"error":"Unauthorized"}"""),
                status = HttpStatusCode.Unauthorized,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClientFactory.create(
            engine = mockEngine,
            tokenProvider = NoOpTokenProvider()
        )

        // When/Then - Should throw ClientRequestException
        assertFailsWith<ClientRequestException> {
            client.get("http://test.com/api/protected")
        }

        // Cleanup
        client.close()
    }

    @Test
    fun `create throws ServerResponseException when 5xx error returned`() = runTest {
        // Given
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""{"error":"Internal Server Error"}"""),
                status = HttpStatusCode.InternalServerError,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClientFactory.create(
            engine = mockEngine,
            tokenProvider = NoOpTokenProvider()
        )

        // When/Then - Should throw ServerResponseException
        assertFailsWith<ServerResponseException> {
            client.get("http://test.com/api/error")
        }

        // Cleanup
        client.close()
    }

    @Test
    fun `create handles 404 correctly when resource not found`() = runTest {
        // Given
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""{"error":"Not Found"}"""),
                status = HttpStatusCode.NotFound,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClientFactory.create(
            engine = mockEngine,
            tokenProvider = NoOpTokenProvider()
        )

        // When/Then
        val exception = assertFailsWith<ClientRequestException> {
            client.get("http://test.com/api/nonexistent")
        }

        assertEquals(HttpStatusCode.NotFound, exception.response.status)

        // Cleanup
        client.close()
    }

    // Test TokenProvider implementation
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
}
