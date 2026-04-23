package pt.dourobats.app.features.settings.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import pt.dourobats.app.features.settings.api.model.Language
import pt.dourobats.app.features.settings.api.model.Theme
import pt.dourobats.app.features.login.api.model.UserProfile
import pt.dourobats.app.features.settings.repository.SettingsRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class SetThemeUseCaseTest {

    @Test
    fun `invoke persists theme to repository`() = runTest {
        val repository = FakeSettingsRepository(initialTheme = Theme.LIGHT)
        val useCase = SetThemeUseCaseImpl(repository)

        useCase(Theme.DARK)

        assertEquals(Theme.DARK, repository.getTheme())
    }

    @Test
    fun `invoke can toggle theme back and forth`() = runTest {
        val repository = FakeSettingsRepository(initialTheme = Theme.LIGHT)
        val useCase = SetThemeUseCaseImpl(repository)

        useCase(Theme.DARK)
        assertEquals(Theme.DARK, repository.getTheme())

        useCase(Theme.LIGHT)
        assertEquals(Theme.LIGHT, repository.getTheme())
    }

    private class FakeSettingsRepository(
        initialTheme: Theme = Theme.LIGHT
    ) : SettingsRepository {
        private val _language = MutableStateFlow(Language.ENGLISH_US)
        private val _theme = MutableStateFlow(initialTheme)
        private val _profile = MutableStateFlow(UserProfile.empty())
        override val languageFlow: Flow<Language> = _language
        override val themeFlow: Flow<Theme> = _theme
        override val userProfileFlow: Flow<UserProfile> = _profile
        override suspend fun setLanguage(language: Language) { _language.value = language }
        override suspend fun getLanguage(): Language = _language.value
        override suspend fun setTheme(theme: Theme) { _theme.value = theme }
        override suspend fun getTheme(): Theme = _theme.value
        override suspend fun updateUserProfile(profile: UserProfile) { _profile.value = profile }
        override suspend fun getUserProfile(): UserProfile = _profile.value
    }
}
