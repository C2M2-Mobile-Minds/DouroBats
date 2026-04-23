package pt.dourobats.app.features.login.repository

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.api.model.AuthState

internal interface AuthRepository {
    val authStateFlow: Flow<AuthState>
    suspend fun requestLoginCode(email: String): Result<Unit>
    suspend fun verifyLoginCode(email: String, code: String): Result<Unit>
    suspend fun logout()
    suspend fun isAuthenticated(): Boolean
}
