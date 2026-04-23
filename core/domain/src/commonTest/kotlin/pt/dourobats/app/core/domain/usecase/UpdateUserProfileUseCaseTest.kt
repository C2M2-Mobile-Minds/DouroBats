package pt.dourobats.app.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import pt.dourobats.app.features.settings.api.model.Language
import pt.dourobats.app.features.settings.api.model.Theme
import pt.dourobats.app.features.login.api.model.UserProfile
import pt.dourobats.app.features.settings.api.repository.SettingsRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateUserProfileUseCaseTest {

    @Test
    fun `invoke persists profile to repository`() = runTest {
        val repository = FakeSettingsRepository()
        val useCase = UpdateUserProfileUseCaseImpl(repository)
        val profile = UserProfile(displayName = "João", email = "joao@test.com", phoneNumber = "+351910000001")

        useCase(profile)

        assertEquals(profile, repository.getUserProfile())
    }

    @Test
    fun `invoke overwrites previous profile`() = runTest {
        val initial = UserProfile(displayName = "Old", email = "old@test.com", phoneNumber = "+351900000000")
        val repository = FakeSettingsRepository(initialProfile = initial)
        val useCase = UpdateUserProfileUseCaseImpl(repository)
        val updated = UserProfile(displayName = "New", email = "new@test.com", phoneNumber = "+351911111111")

        useCase(updated)

        assertEquals(updated, repository.getUserProfile())
    }

    private class FakeSettingsRepository(
        initialProfile: UserProfile = UserProfile.empty()
    ) : SettingsRepository {
        private val _language = MutableStateFlow(Language.ENGLISH_US)
        private val _theme = MutableStateFlow(Theme.LIGHT)
        private val _profile = MutableStateFlow(initialProfile)
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
