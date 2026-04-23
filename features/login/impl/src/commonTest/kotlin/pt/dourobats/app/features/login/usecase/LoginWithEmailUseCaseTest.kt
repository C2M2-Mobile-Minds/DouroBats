package pt.dourobats.app.features.login.usecase

import kotlinx.coroutines.test.runTest
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.core.common.exception.NetworkException
import pt.dourobats.app.core.common.exception.ValidationException
import pt.dourobats.app.features.login.api.exception.InvalidEmail
import pt.dourobats.app.features.login.api.exception.InvalidPassword
import pt.dourobats.app.features.login.repository.AuthRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoginWithEmailUseCaseTest {

    @Test
    fun `loginWithEmail with valid credentials returns success`() = runTest {
        // Given
        val repository = FakeAuthRepository(shouldSucceed = true)
        val useCase = LoginWithEmailUseCaseImpl(repository)

        // When
        val result = useCase("test@example.com", "password123")

        // Then
        assertTrue(result is Result.Success)
        assertEquals("test@example.com", repository.lastEmail)
        assertEquals("password123", repository.lastPassword)
    }

    @Test
    fun `loginWithEmail with blank email returns validation error`() = runTest {
        // Given
        val repository = FakeAuthRepository(shouldSucceed = true)
        val useCase = LoginWithEmailUseCaseImpl(repository)

        // When
        val result = useCase("   ", "password123")

        // Then
        assertTrue(result is Result.Error)
        assertTrue(result.exception is ValidationException.RequiredField)
    }

    @Test
    fun `loginWithEmail with empty email returns validation error`() = runTest {
        // Given
        val repository = FakeAuthRepository(shouldSucceed = true)
        val useCase = LoginWithEmailUseCaseImpl(repository)

        // When
        val result = useCase("", "password123")

        // Then
        assertTrue(result is Result.Error)
        assertTrue(result.exception is ValidationException.RequiredField)
    }

    @Test
    fun `loginWithEmail with invalid email format returns validation error`() = runTest {
        // Given
        val repository = FakeAuthRepository(shouldSucceed = true)
        val useCase = LoginWithEmailUseCaseImpl(repository)

        // When - invalid email formats
        val result1 = useCase("notanemail", "password123")
        val result2 = useCase("missing@domain", "password123")
        val result3 = useCase("@nodomain.com", "password123")
        val result4 = useCase("no.at.sign.com", "password123")

        // Then
        assertTrue(result1 is Result.Error)
        assertTrue(result1.exception is InvalidEmail)
        assertTrue(result2 is Result.Error)
        assertTrue(result2.exception is InvalidEmail)
        assertTrue(result3 is Result.Error)
        assertTrue(result3.exception is InvalidEmail)
        assertTrue(result4 is Result.Error)
        assertTrue(result4.exception is InvalidEmail)
    }

    @Test
    fun `loginWithEmail with valid email formats succeeds`() = runTest {
        // Given
        val repository = FakeAuthRepository(shouldSucceed = true)
        val useCase = LoginWithEmailUseCaseImpl(repository)

        // When - valid email formats
        val result1 = useCase("test@example.com", "password123")
        val result2 = useCase("user.name@domain.co.uk", "password123")
        val result3 = useCase("first+last@sub.domain.com", "password123")

        // Then
        assertTrue(result1 is Result.Success)
        assertTrue(result2 is Result.Success)
        assertTrue(result3 is Result.Success)
    }

    @Test
    fun `loginWithEmail trims whitespace from email`() = runTest {
        // Given
        val repository = FakeAuthRepository(shouldSucceed = true)
        val useCase = LoginWithEmailUseCaseImpl(repository)

        // When
        val result = useCase("  test@example.com  ", "password123")

        // Then
        assertTrue(result is Result.Success)
        assertEquals("test@example.com", repository.lastEmail)
    }

    @Test
    fun `loginWithEmail with blank password returns validation error`() = runTest {
        // Given
        val repository = FakeAuthRepository(shouldSucceed = true)
        val useCase = LoginWithEmailUseCaseImpl(repository)

        // When
        val result = useCase("test@example.com", "   ")

        // Then
        assertTrue(result is Result.Error)
        assertTrue(result.exception is ValidationException.RequiredField)
    }

    @Test
    fun `loginWithEmail with password less than 6 characters returns validation error`() = runTest {
        // Given
        val repository = FakeAuthRepository(shouldSucceed = true)
        val useCase = LoginWithEmailUseCaseImpl(repository)

        // When
        val result = useCase("test@example.com", "12345")

        // Then
        assertTrue(result is Result.Error)
        assertTrue(result.exception is InvalidPassword)
    }

    @Test
    fun `loginWithEmail with password exactly 6 characters succeeds`() = runTest {
        // Given
        val repository = FakeAuthRepository(shouldSucceed = true)
        val useCase = LoginWithEmailUseCaseImpl(repository)

        // When
        val result = useCase("test@example.com", "123456")

        // Then
        assertTrue(result is Result.Success)
    }

    @Test
    fun `loginWithEmail propagates repository errors`() = runTest {
        // Given
        val repository = FakeAuthRepository(shouldSucceed = false)
        val useCase = LoginWithEmailUseCaseImpl(repository)

        // When
        val result = useCase("test@example.com", "password123")

        // Then
        assertTrue(result is Result.Error)
        assertTrue(result.exception is NetworkException)
    }

    /**
     * Fake implementation of AuthRepository for testing.
     */
    private class FakeAuthRepository(
        private val shouldSucceed: Boolean
    ) : AuthRepository {
        var lastEmail: String? = null
        var lastPassword: String? = null

        override suspend fun loginWithEmail(email: String, password: String): Result<Unit> {
            lastEmail = email
            lastPassword = password
            return if (shouldSucceed) {
                Result.Success(Unit)
            } else {
                Result.Error(NetworkException("Login failed"))
            }
        }

        override suspend fun loginWithSocial(method: pt.dourobats.app.features.login.api.model.LoginMethod): Result<Unit> {
            return Result.Success(Unit)
        }

        override suspend fun logout() {
            // No-op for test
        }

        override suspend fun isAuthenticated(): Boolean {
            return false
        }

        override val authStateFlow: kotlinx.coroutines.flow.Flow<pt.dourobats.app.features.login.api.model.AuthState>
            get() = kotlinx.coroutines.flow.flowOf(pt.dourobats.app.features.login.api.model.AuthState.Unauthenticated)
    }
}
