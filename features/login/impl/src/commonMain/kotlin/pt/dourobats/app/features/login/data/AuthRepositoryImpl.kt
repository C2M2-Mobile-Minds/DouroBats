package pt.dourobats.app.features.login.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.api.exception.AuthException
import pt.dourobats.app.features.login.api.model.AuthState
import pt.dourobats.app.features.login.repository.AuthRepository

internal class AuthRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : AuthRepository {

    private val isAuthenticatedKey = booleanPreferencesKey("is_authenticated")
    private val userIdKey = stringPreferencesKey("user_id")
    private val userEmailKey = stringPreferencesKey("user_email")

    private val MOCK_USER_ID = "mock_user_001"

    override val authStateFlow: Flow<AuthState> = dataStore.data
        .map { preferences ->
            val isAuthenticated = preferences[isAuthenticatedKey] ?: false
            if (isAuthenticated) {
                val userId = preferences[userIdKey] ?: ""
                val email = preferences[userEmailKey] ?: ""
                AuthState.Authenticated(userId = userId, email = email)
            } else {
                AuthState.Unauthenticated
            }
        }

    override suspend fun requestLoginCode(email: String): Result<Unit> {
        return try {
            // Mock: always succeed — real impl sends email via API
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(AuthException.Unknown(e.message ?: "Failed to send code"))
        }
    }

    override suspend fun verifyLoginCode(email: String, code: String): Result<Unit> {
        return try {
            if (code == "123456") {
                dataStore.edit { preferences ->
                    preferences[isAuthenticatedKey] = true
                    preferences[userIdKey] = MOCK_USER_ID
                    preferences[userEmailKey] = email
                }
                Result.Success(Unit)
            } else {
                Result.Error(AuthException.InvalidCredentials())
            }
        } catch (e: Exception) {
            Result.Error(AuthException.Unknown(e.message ?: "Verification failed"))
        }
    }

    override suspend fun logout() {
        try {
            dataStore.edit { preferences ->
                preferences.remove(isAuthenticatedKey)
                preferences.remove(userIdKey)
                preferences.remove(userEmailKey)
            }
        } catch (e: Exception) {
            println("Error during logout: ${e.message}")
        }
    }

    override suspend fun isAuthenticated(): Boolean {
        return dataStore.data.first()[isAuthenticatedKey] ?: false
    }
}
