package pt.dourobats.app.features.settings.ui

import pt.dourobats.app.features.login.api.model.UserRole
import pt.dourobats.app.core.localization.Language
import pt.dourobats.app.features.settings.api.model.Theme

sealed interface SettingsAction {
    data class SetLanguage(val language: Language) : SettingsAction
    data class SetTheme(val theme: Theme) : SettingsAction
    data class UpdateDisplayName(val displayName: String) : SettingsAction
    data class UpdateEmail(val email: String) : SettingsAction
    data class UpdatePhoneNumber(val phoneNumber: String) : SettingsAction
    data object SaveProfile : SettingsAction
    data object CancelEdit : SettingsAction
    data class SetRole(val role: UserRole) : SettingsAction
    data object Logout : SettingsAction
}
