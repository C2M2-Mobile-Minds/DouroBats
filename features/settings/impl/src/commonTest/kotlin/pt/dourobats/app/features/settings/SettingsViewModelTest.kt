package pt.dourobats.app.features.settings

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import pt.dourobats.app.features.login.api.model.UserProfile
import pt.dourobats.app.features.login.testing.FakeLogoutUseCase
import pt.dourobats.app.core.localization.Language
import pt.dourobats.app.features.settings.api.model.Theme
import pt.dourobats.app.features.settings.testing.FakeObserveLanguageUseCase
import pt.dourobats.app.features.settings.testing.FakeObserveThemeUseCase
import pt.dourobats.app.features.settings.testing.FakeObserveUserProfileUseCase
import pt.dourobats.app.features.settings.testing.FakeSetLanguageUseCase
import pt.dourobats.app.features.settings.testing.FakeSetThemeUseCase
import pt.dourobats.app.features.settings.testing.FakeUpdateUserProfileUseCase
import pt.dourobats.app.features.settings.ui.SettingsViewModel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var observeUserProfile: FakeObserveUserProfileUseCase
    private lateinit var observeLanguage: FakeObserveLanguageUseCase
    private lateinit var observeTheme: FakeObserveThemeUseCase
    private lateinit var setLanguage: FakeSetLanguageUseCase
    private lateinit var setTheme: FakeSetThemeUseCase
    private lateinit var updateUserProfile: FakeUpdateUserProfileUseCase
    private lateinit var logoutUseCase: FakeLogoutUseCase
    private lateinit var viewModel: SettingsViewModel

    // Shared flows for observe fakes
    private val profileFlow = MutableStateFlow(UserProfile.empty())
    private val languageFlow = MutableStateFlow(Language.ENGLISH_US)
    private val themeFlow = MutableStateFlow(Theme.LIGHT)

    // Captured state for action fakes
    private var lastSetLanguage: Language? = null
    private var setLanguageCount = 0
    private var lastSetTheme: Theme? = null
    private var setThemeCount = 0
    private var lastUpdateProfile: UserProfile? = null
    private var updateProfileCount = 0
    private var logoutInvoked = false

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Reset flows and captured state
        profileFlow.value = UserProfile.empty()
        languageFlow.value = Language.ENGLISH_US
        themeFlow.value = Theme.LIGHT
        lastSetLanguage = null; setLanguageCount = 0
        lastSetTheme = null; setThemeCount = 0
        lastUpdateProfile = null; updateProfileCount = 0
        logoutInvoked = false

        observeUserProfile = FakeObserveUserProfileUseCase().apply { invoke = { profileFlow } }
        observeLanguage = FakeObserveLanguageUseCase().apply { invoke = { languageFlow } }
        observeTheme = FakeObserveThemeUseCase().apply { invoke = { themeFlow } }
        setLanguage = FakeSetLanguageUseCase().apply {
            invoke = { lang -> lastSetLanguage = lang; setLanguageCount++ }
        }
        setTheme = FakeSetThemeUseCase().apply {
            invoke = { t -> lastSetTheme = t; setThemeCount++ }
        }
        updateUserProfile = FakeUpdateUserProfileUseCase().apply {
            invoke = { p -> lastUpdateProfile = p; updateProfileCount++ }
        }
        logoutUseCase = FakeLogoutUseCase().apply { invoke = { logoutInvoked = true } }

        viewModel = SettingsViewModel(
            observeUserProfile = observeUserProfile.build(),
            observeLanguage = observeLanguage.build(),
            observeTheme = observeTheme.build(),
            setLanguageUseCase = setLanguage.build(),
            setThemeUseCase = setTheme.build(),
            updateUserProfileUseCase = updateUserProfile.build(),
            logoutUseCase = logoutUseCase.build()
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `currentLanguage is ENGLISH_US when initialized`() = runTest(testDispatcher) {
        val language = viewModel.currentLanguage.value

        assertEquals(Language.ENGLISH_US, language)
    }

    @Test
    fun `setLanguage delegates to SetLanguageUseCase`() = runTest(testDispatcher) {
        viewModel.setLanguage(Language.PORTUGUESE_BR)
        advanceUntilIdle()

        assertEquals(Language.PORTUGUESE_BR, lastSetLanguage)
        assertEquals(1, setLanguageCount)
    }

    @Test
    fun `currentLanguage updates when observeLanguage flow emits`() = runTest(testDispatcher) {
        val collectorJob = launch { viewModel.currentLanguage.collect {} }

        languageFlow.value = Language.SPANISH
        advanceUntilIdle()

        assertEquals(Language.SPANISH, viewModel.currentLanguage.value)
        collectorJob.cancel()
    }

    @Test
    fun `setLanguage called for every language value`() = runTest(testDispatcher) {
        Language.entries.forEach { language ->
            viewModel.setLanguage(language)
            advanceUntilIdle()
        }

        assertEquals(Language.entries.size, setLanguageCount)
    }

    @Test
    fun `setTheme delegates to SetThemeUseCase`() = runTest(testDispatcher) {
        viewModel.setTheme(Theme.DARK)
        advanceUntilIdle()

        assertEquals(Theme.DARK, lastSetTheme)
        assertEquals(1, setThemeCount)
    }

    @Test
    fun `uiState contains user profile when flow emits`() = runTest(testDispatcher) {
        val testProfile = UserProfile(displayName = "Test User", email = "test@example.com", phoneNumber = "+351912345678")
        profileFlow.value = testProfile

        val collectorJob = launch { viewModel.uiState.collect {} }
        advanceUntilIdle()

        assertEquals(testProfile, viewModel.uiState.value.userProfile)
        collectorJob.cancel()
    }

    @Test
    fun `uiState contains theme when flow emits`() = runTest(testDispatcher) {
        themeFlow.value = Theme.DARK

        val collectorJob = launch { viewModel.uiState.collect {} }
        advanceUntilIdle()

        assertEquals(Theme.DARK, viewModel.uiState.value.currentTheme)
        collectorJob.cancel()
    }

    @Test
    fun `saveProfile delegates to UpdateUserProfileUseCase when valid data`() = runTest(testDispatcher) {
        val initial = UserProfile(displayName = "Old", email = "old@example.com", phoneNumber = "+351000000000")
        profileFlow.value = initial

        val collectorJob = launch { viewModel.uiState.collect {} }
        advanceUntilIdle()

        viewModel.enterEditMode()
        viewModel.updateDisplayName("João Silva")
        viewModel.updateEmail("joao@example.com")
        viewModel.updatePhoneNumber("+351912345678")
        viewModel.saveProfile()
        advanceUntilIdle()

        assertEquals("João Silva", lastUpdateProfile?.displayName)
        assertEquals("joao@example.com", lastUpdateProfile?.email)
        assertEquals("+351912345678", lastUpdateProfile?.phoneNumber)
        assertEquals(1, updateProfileCount)
        collectorJob.cancel()
    }

    @Test
    fun `saveProfile does not call use case when display name is blank`() = runTest(testDispatcher) {
        val initial = UserProfile(displayName = "Old", email = "old@example.com", phoneNumber = "+351000000000")
        profileFlow.value = initial

        val collectorJob = launch { viewModel.uiState.collect {} }
        advanceUntilIdle()

        viewModel.enterEditMode()
        viewModel.updateDisplayName("")
        viewModel.saveProfile()
        advanceUntilIdle()

        assertEquals(0, updateProfileCount)
        collectorJob.cancel()
    }

    @Test
    fun `saveProfile trims whitespace before delegating`() = runTest(testDispatcher) {
        val initial = UserProfile(displayName = "Old", email = "old@example.com", phoneNumber = "+351000000000")
        profileFlow.value = initial

        val collectorJob = launch { viewModel.uiState.collect {} }
        advanceUntilIdle()

        viewModel.enterEditMode()
        viewModel.updateDisplayName("  Ana  ")
        viewModel.updateEmail("  ana@example.com  ")
        viewModel.updatePhoneNumber("  +351912345678  ")
        viewModel.saveProfile()
        advanceUntilIdle()

        assertEquals("Ana", lastUpdateProfile?.displayName)
        assertEquals("ana@example.com", lastUpdateProfile?.email)
        collectorJob.cancel()
    }

    @Test
    fun `enterEditMode loads current profile into edit state`() = runTest(testDispatcher) {
        val profile = UserProfile(displayName = "Initial", email = "i@test.com", phoneNumber = "+351111111111")
        profileFlow.value = profile

        val collectorJob = launch { viewModel.uiState.collect {} }
        advanceUntilIdle()

        viewModel.enterEditMode()

        assertEquals("Initial", viewModel.editState.value.displayName)
        assertEquals("+351111111111", viewModel.editState.value.phoneNumber)
        collectorJob.cancel()
    }

    @Test
    fun `cancelEdit clears edit state`() = runTest(testDispatcher) {
        viewModel.enterEditMode()
        viewModel.updateDisplayName("Some Name")

        viewModel.cancelEdit()

        assertEquals("", viewModel.editState.value.displayName)
    }

    @Test
    fun `logout delegates to LogoutUseCase`() = runTest(testDispatcher) {
        viewModel.logout()
        advanceUntilIdle()

        assertTrue(logoutInvoked)
    }
}
