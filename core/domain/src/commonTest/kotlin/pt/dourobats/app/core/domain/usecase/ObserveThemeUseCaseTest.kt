package pt.dourobats.app.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import pt.dourobats.app.features.settings.api.Language
import pt.dourobats.app.features.settings.api.Theme
import pt.dourobats.app.features.login.api.UserProfile
import pt.dourobats.app.features.settings.api.SettingsRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class ObserveThemeUseCaseTest {

    @Test
    fun `invoke returns initial theme from repository`() = runTest {
        val repository = FakeSettingsRepository(initialTheme = Theme.DARK)
        val useCase = ObserveThemeUseCaseImpl(repository)

        val result = useCase().first()

        assertEquals(Theme.DARK, result)
    }

    @Test
    fun `invoke reflects theme change in repository`() = runTest {
        val repository = FakeSettingsRepository(initialTheme = Theme.LIGHT)
        val useCase = ObserveThemeUseCaseImpl(repository)

        repository.setTheme(Theme.DARK)
        val result = useCase().first()

        assertEquals(Theme.DARK, result)
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
