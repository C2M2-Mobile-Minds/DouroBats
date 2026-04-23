package pt.dourobats.app.features.login.repository

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.api.model.AuthState
import pt.dourobats.app.features.login.api.model.LoginMethod

internal interface AuthRepository {
    val authStateFlow: Flow<AuthState>
    suspend fun loginWithEmail(email: String, password: String): Result<Unit>
    suspend fun loginWithSocial(method: LoginMethod): Result<Unit>
    suspend fun logout()
    suspend fun isAuthenticated(): Boolean
}
