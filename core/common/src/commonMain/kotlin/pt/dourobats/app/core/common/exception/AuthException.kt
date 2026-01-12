package pt.dourobats.app.core.common.exception

/**
 * Base exception for authentication-related errors.
 */
sealed class AuthException(message: String) : Exception(message) {
    /**
     * Invalid credentials provided (wrong email or password).
     */
    class InvalidCredentials : AuthException("Invalid email or password")

    /**
     * Account is locked due to security reasons.
     */
    class AccountLocked : AuthException("Account has been locked. Please contact support.")

    /**
     * Too many failed login attempts.
     */
    class TooManyAttempts : AuthException("Too many login attempts. Please try again later.")

    /**
     * Session expired and user needs to re-authenticate.
     */
    class SessionExpired : AuthException("Your session has expired. Please log in again.")

    /**
     * Generic authentication error.
     */
    class Unknown(message: String = "Authentication failed") : AuthException(message)
}

/**
 * Exception thrown when validation fails.
 */
sealed class ValidationException(message: String) : Exception(message) {
    /**
     * Email validation failed.
     */
    class InvalidEmail(message: String = "Invalid email format") : ValidationException(message)

    /**
     * Password validation failed.
     */
    class InvalidPassword(message: String = "Password must be at least 6 characters") : ValidationException(message)

    /**
     * Required field is empty.
     */
    class RequiredField(fieldName: String) : ValidationException("$fieldName is required")

    /**
     * Generic validation error.
     */
    class Generic(message: String) : ValidationException(message)
}

/**
 * Exception thrown when network operations fail.
 */
class NetworkException(
    message: String = "Network error. Please check your connection.",
    cause: Throwable? = null
) : Exception(message, cause)
