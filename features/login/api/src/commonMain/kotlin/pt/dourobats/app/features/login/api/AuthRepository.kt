package pt.dourobats.app.features.login.api

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.core.common.Result

/**
 * Repository for managing user authentication state.
 *
 * This repository provides access to authentication operations and state.
 * It follows the Repository pattern from Clean Architecture, abstracting
 * data sources from the domain layer.
 */
interface AuthRepository {
    /**
     * Flow of the current authentication state.
     * Emits whenever the authentication state changes.
     *
     * Use this flow to reactively update UI based on authentication status.
     */
    val authStateFlow: Flow<AuthState>

    /**
     * Attempts to log in with email and password.
     *
     * @param email User's email address (should be validated before calling)
     * @param password User's password (should be validated before calling)
     * @return Result.Success if login successful, Result.Error with exception otherwise
     */
    suspend fun loginWithEmail(email: String, password: String): Result<Unit>

    /**
     * Attempts to log in using a social authentication provider.
     *
     * @param method The social login method to use (Google, Facebook, or Apple)
     * @return Result.Success if login successful, Result.Error with exception otherwise
     */
    suspend fun loginWithSocial(method: LoginMethod): Result<Unit>

    /**
     * Logs out the current user and clears authentication state.
     *
     * This operation always succeeds locally, even if server logout fails.
     */
    suspend fun logout()

    /**
     * Checks if a user is currently authenticated.
     *
     * @return true if authenticated, false otherwise
     */
    suspend fun isAuthenticated(): Boolean
}
