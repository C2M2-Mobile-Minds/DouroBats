package pt.dourobats.app.features.settings

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import pt.dourobats.app.core.model.Language
import pt.dourobats.app.core.model.Theme
import pt.dourobats.app.core.model.UserProfile
import pt.dourobats.app.core.repository.SettingsRepository
import pt.dourobats.app.core.test.fakes.fakeSettingsRepository
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: SettingsRepository
    private lateinit var logoutUseCase: pt.dourobats.app.core.test.fakes.FakeLogoutUseCase
    private lateinit var viewModel: SettingsViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = fakeSettingsRepository {
            initialLanguage = Language.ENGLISH_US
        }
        logoutUseCase = pt.dourobats.app.core.test.fakes.FakeLogoutUseCase()
        viewModel = SettingsViewModel(repository, logoutUseCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `currentLanguage is ENGLISH_US when initialized`() = runTest(testDispatcher) {
        // Arrange (done in setup)
        // Act
        val language = viewModel.currentLanguage.value

        // Assert
        assertEquals(Language.ENGLISH_US, language)
    }

    @Test
    fun `repository is updated when setLanguage called`() = runTest(testDispatcher) {
        // Arrange
        val targetLanguage = Language.PORTUGUESE_BR

        // Act
        viewModel.setLanguage(targetLanguage)
        advanceUntilIdle()

        // Assert
        val language = repository.getLanguage()
        assertEquals(targetLanguage, language)
    }

    @Test
    fun `currentLanguage is updated when setLanguage called`() = runTest(testDispatcher) {
        // Arrange
        val collectorJob = launch {
            viewModel.currentLanguage.collect {}
        }
        val targetLanguage = Language.SPANISH

        // Act
        viewModel.setLanguage(targetLanguage)
        advanceUntilIdle()

        // Assert
        assertEquals(targetLanguage, viewModel.currentLanguage.value)
        collectorJob.cancel()
    }

    @Test
    fun `currentLanguage is updated when repository changes`() = runTest(testDispatcher) {
        // Arrange
        val collectorJob = launch {
            viewModel.currentLanguage.collect {}
        }
        val targetLanguage = Language.ENGLISH_GB

        // Act
        repository.setLanguage(targetLanguage)
        advanceUntilIdle()

        // Assert
        assertEquals(targetLanguage, viewModel.currentLanguage.value)
        collectorJob.cancel()
    }

    @Test
    fun `currentLanguage is correct when multiple setLanguage called`() = runTest(testDispatcher) {
        // Arrange
        val collectorJob = launch {
            viewModel.currentLanguage.collect {}
        }

        // Act & Assert
        viewModel.setLanguage(Language.PORTUGUESE_BR)
        advanceUntilIdle()
        assertEquals(Language.PORTUGUESE_BR, viewModel.currentLanguage.value)

        viewModel.setLanguage(Language.SPANISH)
        advanceUntilIdle()
        assertEquals(Language.SPANISH, viewModel.currentLanguage.value)

        viewModel.setLanguage(Language.ENGLISH_GB)
        advanceUntilIdle()
        assertEquals(Language.ENGLISH_GB, viewModel.currentLanguage.value)

        collectorJob.cancel()
    }

    @Test
    fun `setLanguage is successful when all languages tested`() = runTest(testDispatcher) {
        // Arrange
        val collectorJob = launch {
            viewModel.currentLanguage.collect {}
        }

        // Act & Assert
        Language.entries.forEach { language ->
            viewModel.setLanguage(language)
            advanceUntilIdle()

            val current = viewModel.currentLanguage.value
            assertEquals(language, current, "Language $language should be set correctly")
        }

        collectorJob.cancel()
    }

    @Test
    fun `currentLanguage is updated when same language set twice`() = runTest(testDispatcher) {
        // Arrange
        val collectorJob = launch {
            viewModel.currentLanguage.collect {}
        }
        val targetLanguage = Language.PORTUGUESE_PT

        // Act
        viewModel.setLanguage(targetLanguage)
        advanceUntilIdle()
        assertEquals(targetLanguage, viewModel.currentLanguage.value)

        viewModel.setLanguage(targetLanguage)
        advanceUntilIdle()

        // Assert
        assertEquals(targetLanguage, viewModel.currentLanguage.value)
        collectorJob.cancel()
    }

    @Test
    fun `uiState contains user profile when repository has data`() = runTest(testDispatcher) {
        // Arrange
        val testProfile = UserProfile(
            displayName = "Test User",
            email = "test@example.com",
            phoneNumber = "+351912345678",
            profileImageUrl = null
        )
        repository = fakeSettingsRepository {
            initialUserProfile = testProfile
        }
        logoutUseCase = pt.dourobats.app.core.test.fakes.FakeLogoutUseCase()
        viewModel = SettingsViewModel(repository, logoutUseCase)

        val collectorJob = launch {
            viewModel.uiState.collect {}
        }

        // Act
        advanceUntilIdle()

        // Assert
        assertEquals(testProfile, viewModel.uiState.value.userProfile)
        collectorJob.cancel()
    }

    @Test
    fun `setTheme updates repository when DARK provided`() = runTest(testDispatcher) {
        // Arrange
        val targetTheme = Theme.DARK

        // Act
        viewModel.setTheme(targetTheme)
        advanceUntilIdle()

        // Assert
        val theme = repository.getTheme()
        assertEquals(targetTheme, theme)
    }

    @Test
    fun `uiState contains theme when repository has data`() = runTest(testDispatcher) {
        // Arrange
        val testTheme = Theme.LIGHT
        repository = fakeSettingsRepository {
            initialTheme = testTheme
        }
        logoutUseCase = pt.dourobats.app.core.test.fakes.FakeLogoutUseCase()
        viewModel = SettingsViewModel(repository, logoutUseCase)

        val collectorJob = launch {
            viewModel.uiState.collect {}
        }

        // Act
        advanceUntilIdle()

        // Assert
        assertEquals(testTheme, viewModel.uiState.value.currentTheme)
        collectorJob.cancel()
    }

    @Test
    fun `saveProfile updates repository when valid data provided`() = runTest(testDispatcher) {
        // Arrange
        val testProfile = UserProfile(
            displayName = "Old Name",
            email = "old@example.com",
            phoneNumber = "+351000000000",
            profileImageUrl = null
        )
        repository = fakeSettingsRepository {
            initialUserProfile = testProfile
        }
        logoutUseCase = pt.dourobats.app.core.test.fakes.FakeLogoutUseCase()
        viewModel = SettingsViewModel(repository, logoutUseCase)

        val collectorJob = launch {
            viewModel.uiState.collect {}
        }
        advanceUntilIdle()

        viewModel.enterEditMode()
        viewModel.updateDisplayName("John Doe")
        viewModel.updateEmail("john@example.com")
        viewModel.updatePhoneNumber("+351912345678")

        // Act
        viewModel.saveProfile()
        advanceUntilIdle()

        // Assert
        val profile = repository.getUserProfile()
        assertEquals("John Doe", profile.displayName)
        assertEquals("john@example.com", profile.email)
        assertEquals("+351912345678", profile.phoneNumber)
        collectorJob.cancel()
    }

    @Test
    fun `enterEditMode loads current profile into edit state`() = runTest(testDispatcher) {
        // Arrange
        val testProfile = UserProfile(
            displayName = "Initial Name",
            email = "test@example.com",
            phoneNumber = "+351111111111",
            profileImageUrl = null
        )
        repository = fakeSettingsRepository {
            initialUserProfile = testProfile
        }
        logoutUseCase = pt.dourobats.app.core.test.fakes.FakeLogoutUseCase()
        viewModel = SettingsViewModel(repository, logoutUseCase)

        val collectorJob = launch {
            viewModel.uiState.collect {}
        }
        advanceUntilIdle()

        // Act
        viewModel.enterEditMode()

        // Assert
        assertEquals("Initial Name", viewModel.editState.value.displayName)
        assertEquals("+351111111111", viewModel.editState.value.phoneNumber)
        collectorJob.cancel()
    }

    @Test
    fun `updateDisplayName updates edit state`() = runTest(testDispatcher) {
        // Arrange
        viewModel.enterEditMode()

        // Act
        viewModel.updateDisplayName("New Name")

        // Assert
        assertEquals("New Name", viewModel.editState.value.displayName)
    }

    @Test
    fun `updatePhoneNumber updates edit state`() = runTest(testDispatcher) {
        // Arrange
        viewModel.enterEditMode()

        // Act
        viewModel.updatePhoneNumber("+351999999999")

        // Assert
        assertEquals("+351999999999", viewModel.editState.value.phoneNumber)
    }

    @Test
    fun `cancelEdit clears edit state`() = runTest(testDispatcher) {
        // Arrange
        viewModel.enterEditMode()
        viewModel.updateDisplayName("Some Name")
        viewModel.updatePhoneNumber("+351123456789")

        // Act
        viewModel.cancelEdit()

        // Assert
        assertEquals("", viewModel.editState.value.displayName)
        assertEquals("", viewModel.editState.value.phoneNumber)
    }

    @Test
    fun `saveProfile trims whitespace from fields`() = runTest(testDispatcher) {
        // Arrange
        val testProfile = UserProfile(
            displayName = "Old Name",
            email = "old@example.com",
            phoneNumber = "+351000000000",
            profileImageUrl = null
        )
        repository = fakeSettingsRepository {
            initialUserProfile = testProfile
        }
        logoutUseCase = pt.dourobats.app.core.test.fakes.FakeLogoutUseCase()
        viewModel = SettingsViewModel(repository, logoutUseCase)

        val collectorJob = launch {
            viewModel.uiState.collect {}
        }
        advanceUntilIdle()

        viewModel.enterEditMode()
        viewModel.updateDisplayName("  John Doe  ")
        viewModel.updateEmail("  john@example.com  ")
        viewModel.updatePhoneNumber("  +351912345678  ")

        // Act
        viewModel.saveProfile()
        advanceUntilIdle()

        // Assert
        val profile = repository.getUserProfile()
        assertEquals("John Doe", profile.displayName)
        assertEquals("john@example.com", profile.email)
        assertEquals("+351912345678", profile.phoneNumber)
        collectorJob.cancel()
    }
}
