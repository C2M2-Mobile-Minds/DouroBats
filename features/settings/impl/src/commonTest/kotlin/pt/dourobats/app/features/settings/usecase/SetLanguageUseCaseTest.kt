package pt.dourobats.app.features.settings.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import pt.dourobats.app.core.localization.Language
import pt.dourobats.app.features.settings.api.model.Theme
import pt.dourobats.app.features.login.api.model.UserProfile
import pt.dourobats.app.features.settings.repository.SettingsRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class SetLanguageUseCaseTest {

    @Test
    fun `invoke persists language to repository`() = runTest {
        val repository = FakeSettingsRepository(initialLanguage = Language.ENGLISH_US)
        val useCase = SetLanguageUseCaseImpl(repository)

        useCase(Language.PORTUGUESE_BR)

        assertEquals(Language.PORTUGUESE_BR, repository.getLanguage())
    }

    @Test
    fun `invoke can change language multiple times`() = runTest {
        val repository = FakeSettingsRepository()
        val useCase = SetLanguageUseCaseImpl(repository)

        useCase(Language.SPANISH)
        assertEquals(Language.SPANISH, repository.getLanguage())

        useCase(Language.ENGLISH_GB)
        assertEquals(Language.ENGLISH_GB, repository.getLanguage())
    }

    private class FakeSettingsRepository(
        initialLanguage: Language = Language.ENGLISH_US
    ) : SettingsRepository {
        private val _language = MutableStateFlow(initialLanguage)
        private val _theme = MutableStateFlow(Theme.LIGHT)
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
