package pt.dourobats.app.core.domain.usecase

import kotlinx.coroutines.test.runTest
import pt.dourobats.app.core.domain.common.Result
import pt.dourobats.app.core.domain.model.LoginMethod
import pt.dourobats.app.core.domain.repository.AuthRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoginWithSocialUseCaseTest {

    @Test
    fun `loginWithSocial with Google method succeeds`() = runTest {
        // Given
        val repository = FakeAuthRepository()
        val useCase = LoginWithSocialUseCase(repository)

        // When
        val result = useCase(LoginMethod.GOOGLE)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(LoginMethod.GOOGLE, repository.lastMethod)
    }

    @Test
    fun `loginWithSocial with Facebook method succeeds`() = runTest {
        // Given
        val repository = FakeAuthRepository()
        val useCase = LoginWithSocialUseCase(repository)

        // When
        val result = useCase(LoginMethod.FACEBOOK)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(LoginMethod.FACEBOOK, repository.lastMethod)
    }

    @Test
    fun `loginWithSocial with Apple method succeeds`() = runTest {
        // Given
        val repository = FakeAuthRepository()
        val useCase = LoginWithSocialUseCase(repository)

        // When
        val result = useCase(LoginMethod.APPLE)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(LoginMethod.APPLE, repository.lastMethod)
    }

    @Test
    fun `loginWithSocial with EMAIL method returns error`() = runTest {
        // Given
        val repository = FakeAuthRepository()
        val useCase = LoginWithSocialUseCase(repository)

        // When
        val result = useCase(LoginMethod.EMAIL)

        // Then
        assertTrue(result is Result.Error)
        assertTrue(result.exception is IllegalArgumentException)
        assertTrue(result.exception.message!!.contains("LoginWithEmailUseCase"))
    }

    /**
     * Fake implementation of AuthRepository for testing.
     */
    private class FakeAuthRepository : AuthRepository {
        var lastMethod: LoginMethod? = null

        override suspend fun loginWithEmail(email: String, password: String): Result<Unit> {
            return Result.Success(Unit)
        }

        override suspend fun loginWithSocial(method: LoginMethod): Result<Unit> {
            lastMethod = method
            return Result.Success(Unit)
        }

        override suspend fun logout() {
            // No-op for test
        }

        override suspend fun isAuthenticated(): Boolean {
            return false
        }

        override val authStateFlow: kotlinx.coroutines.flow.Flow<pt.dourobats.app.core.domain.model.AuthState>
            get() = kotlinx.coroutines.flow.flowOf(pt.dourobats.app.core.domain.model.AuthState.Unauthenticated)
    }
}
