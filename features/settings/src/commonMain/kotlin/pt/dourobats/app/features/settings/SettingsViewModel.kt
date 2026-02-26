package pt.dourobats.app.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pt.dourobats.app.core.model.Language
import pt.dourobats.app.core.model.Theme
import pt.dourobats.app.core.model.UserProfile
import pt.dourobats.app.core.model.UserRole
import pt.dourobats.app.core.domain.usecase.LogoutUseCase
import pt.dourobats.app.core.domain.usecase.ObserveLanguageUseCase
import pt.dourobats.app.core.domain.usecase.ObserveThemeUseCase
import pt.dourobats.app.core.domain.usecase.ObserveUserProfileUseCase
import pt.dourobats.app.core.domain.usecase.SetLanguageUseCase
import pt.dourobats.app.core.domain.usecase.SetThemeUseCase
import pt.dourobats.app.core.domain.usecase.UpdateUserProfileUseCase

class SettingsViewModel(
    private val observeUserProfile: ObserveUserProfileUseCase,
    private val observeLanguage: ObserveLanguageUseCase,
    private val observeTheme: ObserveThemeUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    // Edit state for profile fields
    private val _editState = MutableStateFlow(ProfileEditState())
    val editState: StateFlow<ProfileEditState> = _editState

    // Validation errors state
    private val _validationErrors = MutableStateFlow(SettingsUiState.ValidationErrors())

    // Combine all flows into single UI state
    val uiState: StateFlow<SettingsUiState> = combine(
        observeUserProfile(),
        observeLanguage(),
        observeTheme(),
        _validationErrors
    ) { profile, language, theme, validationErrors ->
        SettingsUiState(
            userProfile = profile,
            currentLanguage = language,
            currentTheme = theme,
            isLoading = false,
            validationErrors = validationErrors
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    // Separate StateFlow for backward compatibility
    val currentLanguage: StateFlow<Language> = observeLanguage()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Language.ENGLISH_US
        )

    fun setLanguage(language: Language) {
        viewModelScope.launch { setLanguageUseCase(language) }
    }

    fun setTheme(theme: Theme) {
        viewModelScope.launch { setThemeUseCase(theme) }
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
        _validationErrors.value = validateProfile(_editState.value)
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
            updateUserProfileUseCase(updatedProfile)
            _validationErrors.value = SettingsUiState.ValidationErrors()
            onSuccess()
        }
    }

    fun cancelEdit() {
        _editState.value = ProfileEditState()
        _validationErrors.value = SettingsUiState.ValidationErrors()
    }

    private fun validateProfile(editState: ProfileEditState): SettingsUiState.ValidationErrors {
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
        val digitsOnly = phone.replace(Regex("[^0-9]"), "")
        return digitsOnly.length in 9..15
    }

    fun setRole(role: UserRole) {
        viewModelScope.launch {
            val currentProfile = uiState.value.userProfile ?: return@launch
            updateUserProfileUseCase(currentProfile.copy(roles = listOf(role)))
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }

    fun deleteAccount() {
        // TODO: Implement account deletion when backend ready
    }
}
