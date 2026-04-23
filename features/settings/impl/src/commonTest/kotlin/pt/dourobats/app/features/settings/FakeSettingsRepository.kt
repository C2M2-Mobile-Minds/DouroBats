package pt.dourobats.app.features.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import pt.dourobats.app.features.login.api.model.UserProfile
import pt.dourobats.app.features.settings.api.model.Language
import pt.dourobats.app.features.settings.api.model.Theme
import pt.dourobats.app.features.settings.repository.SettingsRepository

internal class FakeSettingsRepositoryBuilder {
    var initialLanguage: Language = Language.ENGLISH_US
    var initialTheme: Theme = Theme.LIGHT
    var initialUserProfile: UserProfile = UserProfile.empty()

    fun build(): SettingsRepository {
        return FakeSettingsRepositoryImpl(initialLanguage, initialTheme, initialUserProfile)
    }

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

        override suspend fun setLanguage(language: Language) { _languageFlow.value = language }
        override suspend fun getLanguage(): Language = _languageFlow.value
        override suspend fun setTheme(theme: Theme) { _themeFlow.value = theme }
        override suspend fun getTheme(): Theme = _themeFlow.value
        override suspend fun updateUserProfile(profile: UserProfile) { _userProfileFlow.value = profile }
        override suspend fun getUserProfile(): UserProfile = _userProfileFlow.value
    }
}

internal fun fakeSettingsRepository(
    builder: FakeSettingsRepositoryBuilder.() -> Unit = {}
): SettingsRepository {
    return FakeSettingsRepositoryBuilder().apply(builder).build()
}
