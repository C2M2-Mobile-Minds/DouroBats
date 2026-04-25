package pt.dourobats.app.features.login.usecase

import kotlinx.coroutines.test.runTest
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.api.exception.AuthException
import pt.dourobats.app.features.login.repository.AuthRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RequestLoginCodeUseCaseTest {

    private val fakeRepository = object : AuthRepository {
        override val authStateFlow get() = throw NotImplementedError()
        override suspend fun requestLoginCode(email: String): Result<Unit> = Result.Success(Unit)
        override suspend fun verifyLoginCode(email: String, code: String): Result<Unit> = throw NotImplementedError()
        override suspend fun logout() = throw NotImplementedError()
        override suspend fun isAuthenticated(): Boolean = throw NotImplementedError()
    }

    @Test
    fun `delegates to repository and returns success`() = runTest {
        val useCase = RequestLoginCodeUseCaseImpl(fakeRepository)
        val result = useCase("test@example.com")
        assertTrue(result is Result.Success)
    }

    @Test
    fun `delegates email to repository`() = runTest {
        var capturedEmail: String? = null
        val repo = object : AuthRepository {
            override val authStateFlow get() = throw NotImplementedError()
            override suspend fun requestLoginCode(email: String): Result<Unit> {
                capturedEmail = email
                return Result.Success(Unit)
            }
            override suspend fun verifyLoginCode(email: String, code: String): Result<Unit> = throw NotImplementedError()
            override suspend fun logout() = throw NotImplementedError()
            override suspend fun isAuthenticated(): Boolean = throw NotImplementedError()
        }
        val useCase = RequestLoginCodeUseCaseImpl(repo)
        useCase("hello@example.com")
        assertEquals("hello@example.com", capturedEmail)
    }

    @Test
    fun `returns repository error result`() = runTest {
        val errorRepo = object : AuthRepository {
            override val authStateFlow get() = throw NotImplementedError()
            override suspend fun requestLoginCode(email: String): Result<Unit> =
                Result.Error(AuthException.Unknown("network error"))
            override suspend fun verifyLoginCode(email: String, code: String): Result<Unit> = throw NotImplementedError()
            override suspend fun logout() = throw NotImplementedError()
            override suspend fun isAuthenticated(): Boolean = throw NotImplementedError()
        }
        val useCase = RequestLoginCodeUseCaseImpl(errorRepo)
        val result = useCase("test@example.com")
        assertTrue(result is Result.Error)
        assertTrue(result.exception is AuthException.Unknown)
    }
}
