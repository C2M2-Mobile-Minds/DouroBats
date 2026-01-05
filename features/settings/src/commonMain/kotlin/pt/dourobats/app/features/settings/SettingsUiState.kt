package pt.dourobats.app.features.settings

import pt.dourobats.app.core.domain.model.Language
import pt.dourobats.app.core.domain.model.Theme
import pt.dourobats.app.core.domain.model.UserProfile

/**
 * UI state for the Settings screen.
 *
 * @property userProfile Current user profile data
 * @property currentLanguage Currently selected language
 * @property currentTheme Currently selected theme
 * @property isLoading Whether data is being loaded
 * @property validationErrors Validation errors for profile fields
 */
data class SettingsUiState(
    val userProfile: UserProfile = UserProfile.empty(),
    val currentLanguage: Language = Language.ENGLISH_US,
    val currentTheme: Theme = Theme.LIGHT,
    val isLoading: Boolean = false,
    val validationErrors: ValidationErrors = ValidationErrors()
) {
    /**
     * Validation errors for profile fields.
     *
     * @property displayName Error message for display name field
     * @property email Error message for email field
     * @property phoneNumber Error message for phone number field
     */
    data class ValidationErrors(
        val displayName: String? = null,
        val email: String? = null,
        val phoneNumber: String? = null
    ) {
        /**
         * Whether there are any validation errors.
         */
        val hasErrors: Boolean
            get() = displayName != null || email != null || phoneNumber != null
    }
}

/**
 * Edit state for user profile fields.
 * Separate from UI state to track form changes before saving.
 *
 * @property displayName Edited display name
 * @property email Edited email address
 * @property phoneNumber Edited phone number
 * @property profileImageUrl Edited profile image URL
 */
data class ProfileEditState(
    val displayName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val profileImageUrl: String? = null
)
