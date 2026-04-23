package pt.dourobats.app.core.common.exception

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
