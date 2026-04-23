package pt.dourobats.app.features.login.ui

/**
 * Validator for login form fields providing real-time UI feedback.
 *
 * This validator is designed for immediate user feedback during typing.
 * It's more lenient than business-level validation:
 * - Returns null for empty fields (to avoid showing errors immediately)
 * - Provides user-friendly error messages
 * - Focuses on format validation only
 *
 * Business-level validation (in use cases) is more comprehensive and strict.
 */
internal class LoginFormValidator {

    /**
     * Result of form validation.
     *
     * @property emailError Error message for email field, null if valid
     * @property passwordError Error message for password field, null if valid
     */
    data class ValidationResult(
        val emailError: String? = null,
        val passwordError: String? = null
    ) {
        /**
         * True if both email and password are valid.
         */
        val isValid: Boolean
            get() = emailError == null && passwordError == null
    }

    /**
     * Validates email field for UI display.
     *
     * Rules:
     * - Blank emails return null (no error shown yet)
     * - Invalid format returns error message
     *
     * @param email Email to validate
     * @return Error message or null if valid
     */
    fun validateEmail(email: String): String? {
        val trimmed = email.trim()

        return when {
            trimmed.isBlank() -> null // Don't show error for empty field
            !trimmed.matches(EMAIL_REGEX) -> "Invalid email format"
            else -> null
        }
    }

    /**
     * Validates password field for UI display.
     *
     * Rules:
     * - Blank passwords return null (no error shown yet)
     * - Too short passwords return error message
     *
     * @param password Password to validate
     * @return Error message or null if valid
     */
    fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> null // Don't show error for empty field
            password.length < MIN_PASSWORD_LENGTH -> "Password must be at least $MIN_PASSWORD_LENGTH characters"
            else -> null
        }
    }

    /**
     * Validates both email and password together.
     *
     * @param email Email to validate
     * @param password Password to validate
     * @return ValidationResult containing any errors
     */
    fun validate(email: String, password: String): ValidationResult {
        return ValidationResult(
            emailError = validateEmail(email),
            passwordError = validatePassword(password)
        )
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        private const val MIN_PASSWORD_LENGTH = 6
    }
}
