package pt.dourobats.app.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import pt.dourobats.app.features.settings.api.Language
import pt.dourobats.app.features.settings.api.Theme
import pt.dourobats.app.features.login.api.UserProfile
import pt.dourobats.app.features.login.api.UserRole
import pt.dourobats.app.features.settings.api.SettingsRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class ObserveUserProfileUseCaseTest {

    @Test
    fun `invoke returns flow from repository`() = runTest {
        val profile = UserProfile(displayName = "Ana", email = "ana@test.com", phoneNumber = "+351910000000")
        val repository = FakeSettingsRepository(initialProfile = profile)
        val useCase = ObserveUserProfileUseCaseImpl(repository)

        val result = useCase().first()

        assertEquals(profile, result)
    }

    @Test
    fun `invoke reflects profile update in repository`() = runTest {
        val repository = FakeSettingsRepository()
        val useCase = ObserveUserProfileUseCaseImpl(repository)
        val updated = UserProfile(displayName = "Carlos", email = "carlos@test.com", phoneNumber = "+351920000000")

        repository.updateUserProfile(updated)
        val result = useCase().first()

        assertEquals(updated, result)
    }

    @Test
    fun `invoke returns profile with committee role`() = runTest {
        val profile = UserProfile.empty().copy(roles = listOf(UserRole.COMMITTEE))
        val repository = FakeSettingsRepository(initialProfile = profile)
        val useCase = ObserveUserProfileUseCaseImpl(repository)

        val result = useCase().first()

        assertEquals(listOf(UserRole.COMMITTEE), result.roles)
    }

    private class FakeSettingsRepository(
        initialProfile: UserProfile = UserProfile.empty(),
        initialLanguage: Language = Language.ENGLISH_US,
        initialTheme: Theme = Theme.LIGHT
    ) : SettingsRepository {
        private val _profile = MutableStateFlow(initialProfile)
        private val _language = MutableStateFlow(initialLanguage)
        private val _theme = MutableStateFlow(initialTheme)
        override val userProfileFlow: Flow<UserProfile> = _profile
        override val languageFlow: Flow<Language> = _language
        override val themeFlow: Flow<Theme> = _theme
        override suspend fun updateUserProfile(profile: UserProfile) { _profile.value = profile }
        override suspend fun getUserProfile(): UserProfile = _profile.value
        override suspend fun setLanguage(language: Language) { _language.value = language }
        override suspend fun getLanguage(): Language = _language.value
        override suspend fun setTheme(theme: Theme) { _theme.value = theme }
        override suspend fun getTheme(): Theme = _theme.value
    }
}
