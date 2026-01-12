package pt.dourobats.app.core.domain.repository

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.core.domain.model.Language
import pt.dourobats.app.core.domain.model.Theme
import pt.dourobats.app.core.domain.model.UserProfile

/**
 * Repository for managing application settings and user profile.
 */
interface SettingsRepository {
    /**
     * Flow of the current language setting.
     */
    val languageFlow: Flow<Language>

    /**
     * Save the selected language.
     */
    suspend fun setLanguage(language: Language)

    /**
     * Get the current language synchronously.
     */
    suspend fun getLanguage(): Language

    /**
     * Flow of the current theme setting.
     */
    val themeFlow: Flow<Theme>

    /**
     * Save the selected theme.
     */
    suspend fun setTheme(theme: Theme)

    /**
     * Get the current theme synchronously.
     */
    suspend fun getTheme(): Theme

    /**
     * Flow of the current user profile.
     */
    val userProfileFlow: Flow<UserProfile>

    /**
     * Update the user profile.
     */
    suspend fun updateUserProfile(profile: UserProfile)

    /**
     * Get the current user profile synchronously.
     */
    suspend fun getUserProfile(): UserProfile
}
