package pt.dourobats.app.core.domain.usecase

import pt.dourobats.app.core.domain.repository.AuthRepository

/**
 * Use case for logging out the current user.
 *
 * This use case encapsulates the business logic for logging out,
 * including clearing authentication state and any cached user data.
 *
 * @property authRepository Repository for authentication operations
 */
open class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Logs out the current user.
     *
     * This operation:
     * - Clears authentication state from DataStore
     * - Invalidates any active sessions
     * - Triggers navigation to login screen via authStateFlow
     *
     * Note: This operation always succeeds. Any errors during logout
     * are logged but don't prevent the user from being logged out locally.
     */
    open suspend operator fun invoke() {
        authRepository.logout()
    }
}
