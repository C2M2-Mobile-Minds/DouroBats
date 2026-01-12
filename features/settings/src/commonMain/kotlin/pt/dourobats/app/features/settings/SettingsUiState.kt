package pt.dourobats.app.features.settings

import pt.dourobats.app.core.model.Language
import pt.dourobats.app.core.model.Theme
import pt.dourobats.app.core.model.UserProfile

/**
 * UI state for the Settings screen.
 *
 * @property userProfile Current user profile data (null during initial load)
 * @property currentLanguage Currently selected language (null during initial load)
 * @property currentTheme Currently selected theme (null during initial load)
 * @property isLoading Whether data is being loaded
 * @property validationErrors Validation errors for profile fields
 */
data class SettingsUiState(
    val userProfile: UserProfile? = null,
    val currentLanguage: Language? = null,
    val currentTheme: Theme? = null,
    val isLoading: Boolean = true,
    val validationErrors: ValidationErrors = ValidationErrors()
) {
    /**
     * Whether we have loaded all required data from DataStore.
     */
    val isDataLoaded: Boolean
        get() = userProfile != null && currentLanguage != null && currentTheme != null


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
