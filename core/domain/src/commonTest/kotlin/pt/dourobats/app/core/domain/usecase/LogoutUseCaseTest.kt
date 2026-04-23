package pt.dourobats.app.core.domain.usecase

import kotlinx.coroutines.test.runTest
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.api.LoginMethod
import pt.dourobats.app.features.login.api.AuthRepository
import kotlin.test.Test
import kotlin.test.assertTrue

class LogoutUseCaseTest {

    @Test
    fun `logout calls repository logout`() = runTest {
        // Given
        val repository = FakeAuthRepository()
        val useCase = LogoutUseCaseImpl(repository)

        // When
        useCase()

        // Then
        assertTrue(repository.logoutCalled)
    }

    @Test
    fun `logout delegates to repository logout`() = runTest {
        // Given
        val repository = FakeAuthRepository()
        val useCase = LogoutUseCaseImpl(repository)

        // When
        useCase()

        // Then - verify it was called
        assertTrue(repository.logoutCalled)
    }

    /**
     * Fake implementation of AuthRepository for testing.
     */
    private class FakeAuthRepository(
        private val shouldThrow: Boolean = false
    ) : AuthRepository {
        var logoutCalled = false

        override suspend fun loginWithEmail(email: String, password: String): Result<Unit> {
            return Result.Success(Unit)
        }

        override suspend fun loginWithSocial(method: LoginMethod): Result<Unit> {
            return Result.Success(Unit)
        }

        override suspend fun logout() {
            logoutCalled = true
            if (shouldThrow) {
                throw RuntimeException("Logout failed")
            }
        }

        override suspend fun isAuthenticated(): Boolean {
            return false
        }

        override val authStateFlow: kotlinx.coroutines.flow.Flow<pt.dourobats.app.features.login.api.AuthState>
            get() = kotlinx.coroutines.flow.flowOf(pt.dourobats.app.features.login.api.AuthState.Unauthenticated)
    }
}
