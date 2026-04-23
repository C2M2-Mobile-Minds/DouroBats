package pt.dourobats.app.features.login

/**
 * UI state for the login screen.
 *
 * @property email Current email input value
 * @property password Current password input value
 * @property isPasswordVisible Whether the password is visible or masked
 * @property isLoading Whether a login request is in progress
 * @property errorMessage General error message to display
 * @property emailError Validation error for email field
 * @property passwordError Validation error for password field
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null
) {
    /**
     * Checks if the form is valid for submission.
     * Valid when both fields have no errors and are not empty.
     */
    val isFormValid: Boolean
        get() = email.isNotBlank() &&
                password.isNotBlank() &&
                emailError == null &&
                passwordError == null
}
