package pt.dourobats.app.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.core.model.LoginMethod
import pt.dourobats.app.core.domain.usecase.LoginWithEmailUseCase
import pt.dourobats.app.core.domain.usecase.LoginWithSocialUseCase

/**
 * ViewModel for the login screen.
 *
 * Responsibilities:
 * - Managing UI state (form fields, loading, errors)
 * - Providing real-time form validation for user feedback
 * - Coordinating login use cases
 * - Mapping domain errors to user-friendly messages
 *
 * This ViewModel follows Clean Architecture by:
 * - Depending on use cases (not repositories directly)
 * - Using separate validator for UI-level validation
 * - Using error mapper for presentation-layer concerns
 *
 * @property loginWithEmailUseCase Use case for email/password authentication
 * @property loginWithSocialUseCase Use case for social authentication
 * @property validator Validator for real-time form feedback
 * @property errorMapper Mapper for converting domain errors to UI messages
 */
class LoginViewModel(
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
    private val loginWithSocialUseCase: LoginWithSocialUseCase,
    private val validator: LoginFormValidator = LoginFormValidator(),
    private val errorMapper: LoginErrorMapper = LoginErrorMapper()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    /**
     * Updates the email field and validates it for immediate UI feedback.
     */
    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                errorMessage = null,
                emailError = validator.validateEmail(email)
            )
        }
    }

    /**
     * Updates the password field and validates it for immediate UI feedback.
     */
    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                errorMessage = null,
                passwordError = validator.validatePassword(password)
            )
        }
    }

    /**
     * Toggles password visibility.
     */
    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    /**
     * Attempts to log in with email and password.
     *
     * Flow:
     * 1. Perform UI-level validation
     * 2. If invalid, return early (errors already shown)
     * 3. Show loading state
     * 4. Execute use case (which does business-level validation)
     * 5. Handle result (success = clear password, error = show message)
     */
    fun loginWithEmail() {
        // UI-level validation for immediate feedback
        val validationResult = validator.validate(
            email = _uiState.value.email,
            password = _uiState.value.password
        )

        // Update UI with validation errors
        _uiState.update {
            it.copy(
                emailError = validationResult.emailError,
                passwordError = validationResult.passwordError
            )
        }

        // Don't proceed if form is invalid
        if (!_uiState.value.isFormValid) {
            return
        }

        // Show loading state
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            // Execute use case (business-level validation happens here)
            val result = loginWithEmailUseCase(
                email = _uiState.value.email,
                password = _uiState.value.password
            )

            // Handle result
            when (result) {
                is Result.Success -> {
                    // Clear sensitive data and loading state
                    _uiState.update {
                        it.copy(
                            password = "",
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    // Navigation happens automatically via authStateFlow in App.kt
                }
                is Result.Error -> {
                    // Map domain error to user-friendly message
                    val errorMessage = errorMapper.mapToUserMessage(result.exception)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }
                }
                is Result.Loading -> {
                    // Should not happen in this flow, but handle gracefully
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    /**
     * Attempts to log in using a social authentication provider.
     *
     * @param method The social login method (Google, Facebook, or Apple)
     */
    fun loginWithSocial(method: LoginMethod) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val result = loginWithSocialUseCase(method)

            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = null)
                    }
                    // Navigation happens automatically via authStateFlow
                }
                is Result.Error -> {
                    val errorMessage = errorMapper.mapToUserMessage(result.exception)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }
                }
                is Result.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    /**
     * Clears any error messages.
     * Called when user dismisses an error or starts typing again.
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
