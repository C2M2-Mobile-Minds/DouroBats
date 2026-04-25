package pt.dourobats.app.features.login.usecase

import kotlinx.coroutines.test.runTest
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.repository.AuthRepository
import kotlin.test.Test
import kotlin.test.assertTrue

class LogoutUseCaseTest {

    @Test
    fun `logout calls repository logout`() = runTest {
        val repository = FakeAuthRepository()
        val useCase = LogoutUseCaseImpl(repository)
        useCase()
        assertTrue(repository.logoutCalled)
    }

    @Test
    fun `logout delegates to repository logout`() = runTest {
        val repository = FakeAuthRepository()
        val useCase = LogoutUseCaseImpl(repository)
        useCase()
        assertTrue(repository.logoutCalled)
    }

    private class FakeAuthRepository(
        private val shouldThrow: Boolean = false
    ) : AuthRepository {
        var logoutCalled = false

        override suspend fun requestLoginCode(email: String): Result<Unit> = Result.Success(Unit)

        override suspend fun verifyLoginCode(email: String, code: String): Result<Unit> = Result.Success(Unit)

        override suspend fun logout(): Result<Unit> {
            logoutCalled = true
            return if (shouldThrow) Result.Error(pt.dourobats.app.features.login.api.exception.AuthException.Unknown("Logout failed"))
            else Result.Success(Unit)
        }

        override suspend fun isAuthenticated(): Boolean = false

        override val authStateFlow: kotlinx.coroutines.flow.Flow<pt.dourobats.app.features.login.api.model.AuthState>
            get() = kotlinx.coroutines.flow.flowOf(pt.dourobats.app.features.login.api.model.AuthState.Unauthenticated)
    }
}
