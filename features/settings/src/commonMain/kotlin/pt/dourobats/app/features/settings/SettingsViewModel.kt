package pt.dourobats.app.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pt.dourobats.app.core.domain.model.Language
import pt.dourobats.app.core.domain.model.Theme
import pt.dourobats.app.core.domain.model.UserProfile
import pt.dourobats.app.core.domain.repository.SettingsRepository
import pt.dourobats.app.core.domain.usecase.LogoutUseCase

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    // Edit state for profile fields
    private val _editState = MutableStateFlow(ProfileEditState())
    val editState: StateFlow<ProfileEditState> = _editState

    // Validation errors state
    private val _validationErrors = MutableStateFlow(SettingsUiState.ValidationErrors())

    // Combine all flows into single UI state
    val uiState: StateFlow<SettingsUiState> = combine(
        settingsRepository.userProfileFlow,
        settingsRepository.languageFlow,
        settingsRepository.themeFlow,
        _validationErrors
    ) { profile, language, theme, validationErrors ->
        SettingsUiState(
            userProfile = profile,
            currentLanguage = language,
            currentTheme = theme,
            isLoading = false, // Data loaded once flows emit
            validationErrors = validationErrors
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState() // Nullable values, isLoading = true
    )

    // Separate StateFlow for backward compatibility
    val currentLanguage: StateFlow<Language> = settingsRepository.languageFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Language.ENGLISH_US
        )

    fun setLanguage(language: Language) {
        viewModelScope.launch {
            settingsRepository.setLanguage(language)
        }
    }

    fun setTheme(theme: Theme) {
        viewModelScope.launch {
            settingsRepository.setTheme(theme)
        }
    }

    fun enterEditMode() {
        val currentProfile = uiState.value.userProfile ?: return
        _editState.value = ProfileEditState(
            displayName = currentProfile.displayName,
            email = currentProfile.email,
            phoneNumber = currentProfile.phoneNumber,
            profileImageUrl = currentProfile.profileImageUrl
        )
    }

    fun updateDisplayName(displayName: String) {
        _editState.value = _editState.value.copy(displayName = displayName)
        validateAndUpdateErrors()
    }

    fun updateEmail(email: String) {
        _editState.value = _editState.value.copy(email = email)
        validateAndUpdateErrors()
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _editState.value = _editState.value.copy(phoneNumber = phoneNumber)
        validateAndUpdateErrors()
    }

    private fun validateAndUpdateErrors() {
        val errors = validateProfile(_editState.value)
        _validationErrors.value = errors
    }

    fun saveProfile(onSuccess: () -> Unit = {}) {
        val editState = _editState.value
        val errors = validateProfile(editState)

        if (errors.hasErrors) {
            _validationErrors.value = errors
            return
        }

        viewModelScope.launch {
            val currentProfile = uiState.value.userProfile ?: return@launch
            val updatedProfile = currentProfile.copy(
                displayName = editState.displayName.trim(),
                email = editState.email.trim(),
                phoneNumber = editState.phoneNumber.trim()
            )
            settingsRepository.updateUserProfile(updatedProfile)

            // Clear errors after successful save
            _validationErrors.value = SettingsUiState.ValidationErrors()

            // Notify success
            onSuccess()
        }
    }

    fun cancelEdit() {
        _editState.value = ProfileEditState()
        _validationErrors.value = SettingsUiState.ValidationErrors()
    }

    private fun validateProfile(editState: ProfileEditState): SettingsUiState.ValidationErrors {
        // Trim values before validation to match what will be saved
        val trimmedDisplayName = editState.displayName.trim()
        val trimmedEmail = editState.email.trim()
        val trimmedPhoneNumber = editState.phoneNumber.trim()

        val displayNameError = when {
            trimmedDisplayName.isBlank() -> "Display name is required"
            trimmedDisplayName.length < 2 -> "Display name must be at least 2 characters"
            else -> null
        }

        val emailError = when {
            trimmedEmail.isBlank() -> "Email is required"
            !trimmedEmail.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) -> "Invalid email format"
            else -> null
        }

        val phoneNumberError = when {
            trimmedPhoneNumber.isBlank() -> "Phone number is required"
            !isValidPhoneNumber(trimmedPhoneNumber) -> "Invalid phone number format"
            else -> null
        }

        return SettingsUiState.ValidationErrors(
            displayName = displayNameError,
            email = emailError,
            phoneNumber = phoneNumberError
        )
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        // Basic validation - checks if there are 9-15 digits
        val digitsOnly = phone.replace(Regex("[^0-9]"), "")
        return digitsOnly.length in 9..15
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            // Navigation happens automatically via authStateFlow in App.kt
        }
    }

    fun deleteAccount() {
        // TODO: Implement account deletion when backend ready
        // - Show confirmation dialog (implemented in UI)
        // - Call API to delete account
        // - Clear all local data
        // - Navigate to login screen
        viewModelScope.launch {
            // Placeholder - no action yet
        }
    }
}
