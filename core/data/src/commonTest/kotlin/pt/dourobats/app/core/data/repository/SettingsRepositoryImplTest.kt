package pt.dourobats.app.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okio.FileSystem
import pt.dourobats.app.core.model.Language
import pt.dourobats.app.core.model.Theme
import pt.dourobats.app.core.model.UserProfile
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repository: SettingsRepositoryImpl
    private lateinit var testFile: okio.Path

    @BeforeTest
    fun setup() {
        val tempDir = FileSystem.SYSTEM_TEMPORARY_DIRECTORY
        testFile = tempDir / "test_settings_${Random.nextLong()}.preferences_pb"

        dataStore = PreferenceDataStoreFactory.createWithPath(
            corruptionHandler = null,
            migrations = emptyList(),
            scope = kotlinx.coroutines.CoroutineScope(testDispatcher + kotlinx.coroutines.SupervisorJob()),
            produceFile = { testFile }
        )
        repository = SettingsRepositoryImpl(dataStore)
    }

    @AfterTest
    fun tearDown() {
        // Clean up test file
        try {
            FileSystem.SYSTEM.delete(testFile, mustExist = false)
        } catch (e: Exception) {
            // Ignore if file doesn't exist
        }
    }

    @Test
    fun `languageFlow is system default when no preference set`() = runTest(testDispatcher) {
        // Arrange
        val collectorJob = launch {
            repository.languageFlow.collect {}
        }

        // Act
        advanceUntilIdle()
        val language = repository.languageFlow.first()

        // Assert
        assertEquals(Language.getSystemDefault(), language)
        collectorJob.cancel()
    }

    @Test
    fun `setLanguage is successful when ENGLISH_US provided`() = runTest(testDispatcher) {
        // Arrange
        val targetLanguage = Language.ENGLISH_US

        // Act
        repository.setLanguage(targetLanguage)
        advanceUntilIdle()

        // Assert
        val language = repository.getLanguage()
        assertEquals(targetLanguage, language)
    }

    @Test
    fun `setLanguage is successful when ENGLISH_GB provided`() = runTest(testDispatcher) {
        // Arrange
        val targetLanguage = Language.ENGLISH_GB

        // Act
        repository.setLanguage(targetLanguage)
        advanceUntilIdle()

        // Assert
        val language = repository.getLanguage()
        assertEquals(targetLanguage, language)
    }

    @Test
    fun `setLanguage is successful when PORTUGUESE_BR provided`() = runTest(testDispatcher) {
        // Arrange
        val targetLanguage = Language.PORTUGUESE_BR

        // Act
        repository.setLanguage(targetLanguage)
        advanceUntilIdle()

        // Assert
        val language = repository.getLanguage()
        assertEquals(targetLanguage, language)
    }

    @Test
    fun `setLanguage is successful when PORTUGUESE_PT provided`() = runTest(testDispatcher) {
        // Arrange
        val targetLanguage = Language.PORTUGUESE_PT

        // Act
        repository.setLanguage(targetLanguage)
        advanceUntilIdle()

        // Assert
        val language = repository.getLanguage()
        assertEquals(targetLanguage, language)
    }

    @Test
    fun `setLanguage is successful when SPANISH provided`() = runTest(testDispatcher) {
        // Arrange
        val targetLanguage = Language.SPANISH

        // Act
        repository.setLanguage(targetLanguage)
        advanceUntilIdle()

        // Assert
        val language = repository.getLanguage()
        assertEquals(targetLanguage, language)
    }

    @Test
    fun `getLanguage is correct language when language was set`() = runTest(testDispatcher) {
        // Arrange
        val targetLanguage = Language.PORTUGUESE_BR
        repository.setLanguage(targetLanguage)
        advanceUntilIdle()

        // Act
        val language = repository.getLanguage()

        // Assert
        assertEquals(targetLanguage, language)
    }

    @Test
    fun `languageFlow is updated when setLanguage called`() = runTest(testDispatcher) {
        // Arrange
        val collectorJob = launch {
            repository.languageFlow.collect {}
        }
        val targetLanguage = Language.SPANISH

        // Act
        repository.setLanguage(targetLanguage)
        advanceUntilIdle()

        // Assert
        val language = repository.languageFlow.first()
        assertEquals(targetLanguage, language)
        collectorJob.cancel()
    }

    @Test
    fun `languageFlow is correct when multiple setLanguage called`() = runTest(testDispatcher) {
        // Arrange
        val collectorJob = launch {
            repository.languageFlow.collect {}
        }

        // Act & Assert
        repository.setLanguage(Language.PORTUGUESE_BR)
        advanceUntilIdle()
        assertEquals(Language.PORTUGUESE_BR, repository.languageFlow.first())

        repository.setLanguage(Language.SPANISH)
        advanceUntilIdle()
        assertEquals(Language.SPANISH, repository.languageFlow.first())

        repository.setLanguage(Language.ENGLISH_GB)
        advanceUntilIdle()
        assertEquals(Language.ENGLISH_GB, repository.languageFlow.first())

        collectorJob.cancel()
    }

    @Test
    fun `setLanguage is successful when all languages tested`() = runTest(testDispatcher) {
        // Arrange
        val collectorJob = launch {
            repository.languageFlow.collect {}
        }

        // Act & Assert
        Language.entries.forEach { language ->
            repository.setLanguage(language)
            advanceUntilIdle()

            val current = repository.getLanguage()
            assertEquals(language, current, "Language $language should be persisted correctly")
        }

        collectorJob.cancel()
    }

    @Test
    fun `getLanguage is correct when same language set twice`() = runTest(testDispatcher) {
        // Arrange
        val targetLanguage = Language.PORTUGUESE_PT

        // Act
        repository.setLanguage(targetLanguage)
        advanceUntilIdle()
        val firstResult = repository.getLanguage()

        repository.setLanguage(targetLanguage)
        advanceUntilIdle()
        val secondResult = repository.getLanguage()

        // Assert
        assertEquals(targetLanguage, firstResult)
        assertEquals(targetLanguage, secondResult)
    }

    @Test
    fun `themeFlow is LIGHT when no preference set`() = runTest(testDispatcher) {
        // Arrange
        val collectorJob = launch {
            repository.themeFlow.collect {}
        }

        // Act
        advanceUntilIdle()
        val theme = repository.themeFlow.first()

        // Assert
        assertEquals(Theme.LIGHT, theme)
        collectorJob.cancel()
    }

    @Test
    fun `setTheme is successful when DARK provided`() = runTest(testDispatcher) {
        // Arrange
        val targetTheme = Theme.DARK

        // Act
        repository.setTheme(targetTheme)
        advanceUntilIdle()

        // Assert
        val theme = repository.getTheme()
        assertEquals(targetTheme, theme)
    }

    @Test
    fun `setTheme is successful when LIGHT provided`() = runTest(testDispatcher) {
        // Arrange
        val targetTheme = Theme.LIGHT

        // Act
        repository.setTheme(targetTheme)
        advanceUntilIdle()

        // Assert
        val theme = repository.getTheme()
        assertEquals(targetTheme, theme)
    }

    @Test
    fun `themeFlow is updated when setTheme called`() = runTest(testDispatcher) {
        // Arrange
        val collectorJob = launch {
            repository.themeFlow.collect {}
        }
        val targetTheme = Theme.DARK

        // Act
        repository.setTheme(targetTheme)
        advanceUntilIdle()

        // Assert
        val theme = repository.themeFlow.first()
        assertEquals(targetTheme, theme)
        collectorJob.cancel()
    }

    @Test
    fun `userProfileFlow returns empty profile when no data set`() = runTest(testDispatcher) {
        // Arrange
        val collectorJob = launch {
            repository.userProfileFlow.collect {}
        }

        // Act
        advanceUntilIdle()
        val profile = repository.userProfileFlow.first()

        // Assert
        assertEquals("", profile.displayName)
        assertEquals("", profile.email)
        assertEquals("", profile.phoneNumber)
        assertEquals(null, profile.profileImageUrl)
        collectorJob.cancel()
    }

    @Test
    fun `updateUserProfile is successful when valid profile provided`() = runTest(testDispatcher) {
        // Arrange
        val targetProfile = UserProfile(
            displayName = "John Doe",
            email = "john@example.com",
            phoneNumber = "+351912345678",
            profileImageUrl = null
        )

        // Act
        repository.updateUserProfile(targetProfile)
        advanceUntilIdle()

        // Assert
        val profile = repository.getUserProfile()
        assertEquals(targetProfile, profile)
    }

    @Test
    fun `updateUserProfile is successful when profile with image URL provided`() = runTest(testDispatcher) {
        // Arrange
        val targetProfile = UserProfile(
            displayName = "Jane Smith",
            email = "jane@example.com",
            phoneNumber = "+351987654321",
            profileImageUrl = "https://example.com/avatar.jpg"
        )

        // Act
        repository.updateUserProfile(targetProfile)
        advanceUntilIdle()

        // Assert
        val profile = repository.getUserProfile()
        assertEquals(targetProfile, profile)
    }

    @Test
    fun `userProfileFlow is updated when updateUserProfile called`() = runTest(testDispatcher) {
        // Arrange
        val collectorJob = launch {
            repository.userProfileFlow.collect {}
        }
        val targetProfile = UserProfile(
            displayName = "Test User",
            email = "test@example.com",
            phoneNumber = "+351999999999",
            profileImageUrl = null
        )

        // Act
        repository.updateUserProfile(targetProfile)
        advanceUntilIdle()

        // Assert
        val profile = repository.userProfileFlow.first()
        assertEquals(targetProfile, profile)
        collectorJob.cancel()
    }
}
