package pt.dourobats.app.features.login.usecase

import kotlinx.coroutines.test.runTest
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.api.exception.AuthException
import pt.dourobats.app.features.login.repository.AuthRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VerifyLoginCodeUseCaseTest {

    @Test
    fun `delegates to repository and returns success`() = runTest {
        val repo = object : AuthRepository {
            override val authStateFlow get() = throw NotImplementedError()
            override suspend fun requestLoginCode(email: String): Result<Unit> = throw NotImplementedError()
            override suspend fun verifyLoginCode(email: String, code: String): Result<Unit> = Result.Success(Unit)
            override suspend fun logout() = throw NotImplementedError()
            override suspend fun isAuthenticated(): Boolean = throw NotImplementedError()
        }
        val useCase = VerifyLoginCodeUseCaseImpl(repo)
        val result = useCase("test@example.com", "123456")
        assertTrue(result is Result.Success)
    }

    @Test
    fun `delegates email and code to repository`() = runTest {
        var capturedEmail: String? = null
        var capturedCode: String? = null
        val repo = object : AuthRepository {
            override val authStateFlow get() = throw NotImplementedError()
            override suspend fun requestLoginCode(email: String): Result<Unit> = throw NotImplementedError()
            override suspend fun verifyLoginCode(email: String, code: String): Result<Unit> {
                capturedEmail = email
                capturedCode = code
                return Result.Success(Unit)
            }
            override suspend fun logout() = throw NotImplementedError()
            override suspend fun isAuthenticated(): Boolean = throw NotImplementedError()
        }
        val useCase = VerifyLoginCodeUseCaseImpl(repo)
        useCase("user@example.com", "654321")
        assertEquals("user@example.com", capturedEmail)
        assertEquals("654321", capturedCode)
    }

    @Test
    fun `returns repository error result`() = runTest {
        val repo = object : AuthRepository {
            override val authStateFlow get() = throw NotImplementedError()
            override suspend fun requestLoginCode(email: String): Result<Unit> = throw NotImplementedError()
            override suspend fun verifyLoginCode(email: String, code: String): Result<Unit> =
                Result.Error(AuthException.InvalidCredentials())
            override suspend fun logout() = throw NotImplementedError()
            override suspend fun isAuthenticated(): Boolean = throw NotImplementedError()
        }
        val useCase = VerifyLoginCodeUseCaseImpl(repo)
        val result = useCase("test@example.com", "wrong")
        assertTrue(result is Result.Error)
        assertTrue(result.exception is AuthException.InvalidCredentials)
    }
}
