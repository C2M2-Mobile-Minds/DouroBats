package pt.dourobats.app.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import pt.dourobats.app.core.model.Language
import pt.dourobats.app.core.model.Theme
import pt.dourobats.app.core.model.UserProfile
import pt.dourobats.app.core.repository.SettingsRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class ObserveLanguageUseCaseTest {

    @Test
    fun `invoke returns initial language from repository`() = runTest {
        val repository = FakeSettingsRepository(initialLanguage = Language.PORTUGUESE_PT)
        val useCase = ObserveLanguageUseCaseImpl(repository)

        val result = useCase().first()

        assertEquals(Language.PORTUGUESE_PT, result)
    }

    @Test
    fun `invoke reflects language change in repository`() = runTest {
        val repository = FakeSettingsRepository(initialLanguage = Language.ENGLISH_US)
        val useCase = ObserveLanguageUseCaseImpl(repository)

        repository.setLanguage(Language.SPANISH)
        val result = useCase().first()

        assertEquals(Language.SPANISH, result)
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
