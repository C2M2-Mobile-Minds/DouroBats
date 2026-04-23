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
import pt.dourobats.app.core.common.exception.AuthException
import pt.dourobats.app.features.login.api.model.AuthState
import pt.dourobats.app.features.login.api.model.LoginMethod
import pt.dourobats.app.features.login.repository.AuthRepository

/**
 * Implementation of AuthRepository using DataStore for persistence.
 *
 * Currently uses mock authentication for development.
 * Replace with real API client when backend is ready.
 *
 * This implementation handles:
 * - Persisting authentication state across app restarts
 * - Reactive authentication state via Flow
 * - Mock credential validation (for development)
 *
 * @property dataStore DataStore instance for persisting preferences
 */
internal class AuthRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : AuthRepository {

    private val isAuthenticatedKey = booleanPreferencesKey("is_authenticated")
    private val userIdKey = stringPreferencesKey("user_id")
    private val userEmailKey = stringPreferencesKey("user_email")
    private val loginMethodKey = stringPreferencesKey("login_method")

    // Mock credentials for email login (development only)
    private val MOCK_EMAIL = "user@dourobats.com"
    private val MOCK_PASSWORD = "123456"
    private val MOCK_USER_ID = "mock_user_001"

    override val authStateFlow: Flow<AuthState> = dataStore.data
        .map { preferences ->
            val isAuthenticated = preferences[isAuthenticatedKey] ?: false

            if (isAuthenticated) {
                val userId = preferences[userIdKey] ?: ""
                val email = preferences[userEmailKey] ?: ""
                val methodString = preferences[loginMethodKey] ?: LoginMethod.EMAIL.name
                val loginMethod = try {
                    LoginMethod.valueOf(methodString)
                } catch (e: IllegalArgumentException) {
                    LoginMethod.EMAIL
                }

                AuthState.Authenticated(
                    userId = userId,
                    email = email,
                    loginMethod = loginMethod
                )
            } else {
                AuthState.Unauthenticated
            }
        }

    override suspend fun loginWithEmail(email: String, password: String): Result<Unit> {
        return try {
            // Mock validation - check against hardcoded credentials
            // TODO: Replace with real API call when backend is ready
            if (email == MOCK_EMAIL && password == MOCK_PASSWORD) {
                // Save authenticated state to DataStore
                dataStore.edit { preferences ->
                    preferences[isAuthenticatedKey] = true
                    preferences[userIdKey] = MOCK_USER_ID
                    preferences[userEmailKey] = email
                    preferences[loginMethodKey] = LoginMethod.EMAIL.name
                }
                Result.Success(Unit)
            } else {
                // Return domain exception instead of generic exception
                Result.Error(AuthException.InvalidCredentials())
            }
        } catch (e: Exception) {
            // Wrap unexpected errors in unknown auth exception
            Result.Error(AuthException.Unknown(e.message ?: "Login failed"))
        }
    }

    override suspend fun loginWithSocial(method: LoginMethod): Result<Unit> {
        return try {
            // Mock social login - immediately authenticate
            // TODO: Replace with real OAuth flow when backend is ready
            val email = when (method) {
                LoginMethod.GOOGLE -> "user@gmail.com"
                LoginMethod.FACEBOOK -> "user@facebook.com"
                LoginMethod.APPLE -> "user@apple.com"
                LoginMethod.EMAIL -> {
                    return Result.Error(
                        IllegalArgumentException("Use loginWithEmail for email authentication")
                    )
                }
            }

            dataStore.edit { preferences ->
                preferences[isAuthenticatedKey] = true
                preferences[userIdKey] = MOCK_USER_ID
                preferences[userEmailKey] = email
                preferences[loginMethodKey] = method.name
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(AuthException.Unknown(e.message ?: "Social login failed"))
        }
    }

    override suspend fun logout() {
        try {
            // Clear authentication data from DataStore
            dataStore.edit { preferences ->
                preferences.remove(isAuthenticatedKey)
                preferences.remove(userIdKey)
                preferences.remove(userEmailKey)
                preferences.remove(loginMethodKey)
            }
        } catch (e: Exception) {
            // Log error but don't throw - logout should always succeed locally
            // TODO: Add proper logging when logging framework is set up
            println("Error during logout: ${e.message}")
        }
    }

    override suspend fun isAuthenticated(): Boolean {
        return dataStore.data.first()[isAuthenticatedKey] ?: false
    }
}
