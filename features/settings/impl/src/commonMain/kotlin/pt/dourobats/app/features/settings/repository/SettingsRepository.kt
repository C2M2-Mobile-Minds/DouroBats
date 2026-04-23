package pt.dourobats.app.features.settings.repository

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.features.login.api.model.UserProfile
import pt.dourobats.app.features.settings.api.model.Language
import pt.dourobats.app.features.settings.api.model.Theme

internal interface SettingsRepository {
    val languageFlow: Flow<Language>
    suspend fun setLanguage(language: Language)
    suspend fun getLanguage(): Language
    val themeFlow: Flow<Theme>
    suspend fun setTheme(theme: Theme)
    suspend fun getTheme(): Theme
    val userProfileFlow: Flow<UserProfile>
    suspend fun updateUserProfile(profile: UserProfile)
    suspend fun getUserProfile(): UserProfile
}
