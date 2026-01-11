package pt.dourobats.app.core.domain.usecase

import pt.dourobats.app.core.domain.common.Result
import pt.dourobats.app.core.domain.exception.ValidationException
import pt.dourobats.app.core.domain.repository.AuthRepository

/**
 * Use case for authenticating a user with email and password.
 *
 * This use case encapsulates the business logic for email/password authentication,
 * including validation and error handling. It follows Clean Architecture principles
 * by keeping business logic separate from presentation and data layers.
 *
 * @property authRepository Repository for authentication operations
 */
open class LoginWithEmailUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Authenticates a user with email and password.
     *
     * Performs comprehensive validation before attempting authentication:
     * - Trims whitespace from email
     * - Validates email format
     * - Validates password length
     *
     * @param email User's email address
     * @param password User's password
     * @return Result indicating success or failure with appropriate error
     */
    open suspend operator fun invoke(email: String, password: String): Result<Unit> {
        // Trim email to handle accidental whitespace
        val trimmedEmail = email.trim()

        // Validate email
        if (trimmedEmail.isBlank()) {
            return Result.Error(ValidationException.RequiredField("Email"))
        }

        if (!isValidEmailFormat(trimmedEmail)) {
            return Result.Error(ValidationException.InvalidEmail())
        }

        // Validate password
        if (password.isBlank()) {
            return Result.Error(ValidationException.RequiredField("Password"))
        }

        if (password.length < MIN_PASSWORD_LENGTH) {
            return Result.Error(
                ValidationException.InvalidPassword(
                    "Password must be at least $MIN_PASSWORD_LENGTH characters"
                )
            )
        }

        // Delegate to repository for actual authentication
        return authRepository.loginWithEmail(trimmedEmail, password)
    }

    /**
     * Validates email format using regex.
     *
     * Checks for:
     * - Local part (before @)
     * - @ symbol
     * - Domain part (after @)
     * - Top-level domain (at least 2 characters)
     *
     * @param email Email to validate
     * @return true if email format is valid, false otherwise
     */
    private fun isValidEmailFormat(email: String): Boolean {
        return EMAIL_REGEX.matches(email)
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        private const val MIN_PASSWORD_LENGTH = 6
    }
}
