package pt.dourobats.app.core.test.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import pt.dourobats.app.core.model.Language
import pt.dourobats.app.core.model.Theme
import pt.dourobats.app.core.model.UserProfile
import pt.dourobats.app.core.repository.SettingsRepository

/**
 * Builder for creating fake SettingsRepository instances in tests.
 * Follows the Factory Pattern as defined in project README.
 *
 * Usage:
 * ```
 * val repository = fakeSettingsRepository {
 *     initialLanguage = Language.PORTUGUESE_BR
 * }
 * ```
 */
class FakeSettingsRepositoryBuilder {
    /**
     * The initial language for the repository.
     * Defaults to ENGLISH_US if not specified.
     */
    var initialLanguage: Language = Language.ENGLISH_US

    /**
     * The initial theme for the repository.
     * Defaults to LIGHT if not specified.
     */
    var initialTheme: Theme = Theme.LIGHT

    /**
     * The initial user profile for the repository.
     * Defaults to empty profile if not specified.
     */
    var initialUserProfile: UserProfile = UserProfile.empty()

    /**
     * Builds the fake repository instance.
     */
    fun build(): SettingsRepository {
        return FakeSettingsRepositoryImpl(initialLanguage, initialTheme, initialUserProfile)
    }

    /**
     * Internal implementation of the fake repository.
     */
    private class FakeSettingsRepositoryImpl(
        initialLanguage: Language,
        initialTheme: Theme,
        initialUserProfile: UserProfile
    ) : SettingsRepository {
        private val _languageFlow = MutableStateFlow(initialLanguage)
        private val _themeFlow = MutableStateFlow(initialTheme)
        private val _userProfileFlow = MutableStateFlow(initialUserProfile)

        override val languageFlow: Flow<Language> = _languageFlow
        override val themeFlow: Flow<Theme> = _themeFlow
        override val userProfileFlow: Flow<UserProfile> = _userProfileFlow

        override suspend fun setLanguage(language: Language) {
            _languageFlow.value = language
        }

        override suspend fun getLanguage(): Language {
            return _languageFlow.value
        }

        override suspend fun setTheme(theme: Theme) {
            _themeFlow.value = theme
        }

        override suspend fun getTheme(): Theme {
            return _themeFlow.value
        }

        override suspend fun updateUserProfile(profile: UserProfile) {
            _userProfileFlow.value = profile
        }

        override suspend fun getUserProfile(): UserProfile {
            return _userProfileFlow.value
        }
    }
}

/**
 * Factory function for creating fake SettingsRepository instances.
 * Uses the builder pattern for flexible test configuration.
 *
 * @param builder Lambda with receiver for configuring the repository
 * @return Configured fake SettingsRepository instance
 *
 * Example:
 * ```
 * val repository = fakeSettingsRepository {
 *     initialLanguage = Language.SPANISH
 * }
 * ```
 */
fun fakeSettingsRepository(
    builder: FakeSettingsRepositoryBuilder.() -> Unit = {}
): SettingsRepository {
    return FakeSettingsRepositoryBuilder().apply(builder).build()
}
