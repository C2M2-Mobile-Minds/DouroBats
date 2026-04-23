package pt.dourobats.app.features.login.ui

import pt.dourobats.app.features.login.api.exception.AuthException
import pt.dourobats.app.features.login.api.exception.InvalidEmail
import pt.dourobats.app.features.login.api.exception.InvalidPassword
import pt.dourobats.app.core.common.exception.NetworkException
import pt.dourobats.app.core.common.exception.ValidationException

/**
 * Maps domain exceptions to user-friendly error messages for the login screen.
 *
 * This mapper translates technical exceptions from the domain/data layers
 * into messages that are appropriate to display to end users.
 *
 * Follows the principle of separation of concerns by keeping
 * error message formatting in the presentation layer.
 */
internal class LoginErrorMapper {

    /**
     * Maps a throwable to a user-friendly error message.
     *
     * @param error The exception to map
     * @return A user-friendly error message suitable for display
     */
    fun mapToUserMessage(error: Throwable): String {
        return when (error) {
            // Validation errors
            is InvalidEmail -> "Please enter a valid email address"
            is InvalidPassword -> "Password must be at least 6 characters"
            is ValidationException.RequiredField -> "${error.message}"
            is ValidationException.Generic -> error.message ?: "Please check your input"

            // Authentication errors
            is AuthException.InvalidCredentials -> "Invalid email or password. Please try again."
            is AuthException.AccountLocked -> "Your account has been locked. Please contact support."
            is AuthException.TooManyAttempts -> "Too many login attempts. Please try again in a few minutes."
            is AuthException.SessionExpired -> "Your session has expired. Please log in again."
            is AuthException.Unknown -> "Authentication failed. Please try again."

            // Network errors
            is NetworkException -> "Network error. Please check your internet connection and try again."

            // Illegal argument (misuse of API)
            is IllegalArgumentException -> error.message ?: "Invalid operation"

            // Unknown errors
            else -> "An unexpected error occurred. Please try again."
        }
    }

    /**
     * Determines if an error should be shown immediately or only after user action.
     *
     * Some errors (like network errors) should be shown immediately,
     * while others (like validation errors) might only show after submission.
     *
     * @param error The exception to check
     * @return true if error should be shown immediately
     */
    fun shouldShowImmediately(error: Throwable): Boolean {
        return when (error) {
            is NetworkException -> true
            is AuthException -> true
            else -> false
        }
    }
}
