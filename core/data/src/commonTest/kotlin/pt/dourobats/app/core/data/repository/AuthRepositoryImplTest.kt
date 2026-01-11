package pt.dourobats.app.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okio.FileSystem
import okio.Path.Companion.toPath
import pt.dourobats.app.core.domain.common.Result
import pt.dourobats.app.core.domain.exception.AuthException
import pt.dourobats.app.core.domain.model.AuthState
import pt.dourobats.app.core.domain.model.LoginMethod
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AuthRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repository: AuthRepositoryImpl
    private lateinit var testFile: okio.Path

    @BeforeTest
    fun setup() {
        val tempDir = FileSystem.SYSTEM_TEMPORARY_DIRECTORY
        testFile = tempDir / "test_auth_${kotlin.random.Random.nextLong()}.preferences_pb"

        dataStore = PreferenceDataStoreFactory.createWithPath(
            corruptionHandler = null,
            migrations = emptyList(),
            scope = kotlinx.coroutines.CoroutineScope(testDispatcher + kotlinx.coroutines.SupervisorJob()),
            produceFile = { testFile }
        )
        repository = AuthRepositoryImpl(dataStore)
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
    fun `loginWithEmail with correct credentials returns success`() = runTest(testDispatcher) {
        // When
        val result = repository.loginWithEmail("user@dourobats.com", "123456")

        // Then
        assertTrue(result is Result.Success)
    }

    @Test
    fun `loginWithEmail with incorrect email returns error`() = runTest(testDispatcher) {
        // When
        val result = repository.loginWithEmail("wrong@email.com", "123456")

        // Then
        assertTrue(result is Result.Error)
        assertTrue(result.exception is AuthException.InvalidCredentials)
    }

    @Test
    fun `loginWithEmail with incorrect password returns error`() = runTest(testDispatcher) {
        // When
        val result = repository.loginWithEmail("user@dourobats.com", "wrong_password")

        // Then
        assertTrue(result is Result.Error)
        assertTrue(result.exception is AuthException.InvalidCredentials)
    }

    @Test
    fun `loginWithEmail persists auth state to DataStore`() = runTest(testDispatcher) {
        // When
        repository.loginWithEmail("user@dourobats.com", "123456")

        // Then
        val authState = repository.authStateFlow.first()
        assertTrue(authState is AuthState.Authenticated)
        assertEquals("mock_user_001", authState.userId)
        assertEquals("user@dourobats.com", authState.email)
        assertEquals(LoginMethod.EMAIL, authState.loginMethod)
    }

    @Test
    fun `loginWithSocial with Google succeeds`() = runTest(testDispatcher) {
        // When
        val result = repository.loginWithSocial(LoginMethod.GOOGLE)

        // Then
        assertTrue(result is Result.Success)
        val authState = repository.authStateFlow.first()
        assertTrue(authState is AuthState.Authenticated)
        assertEquals("user@gmail.com", authState.email)
        assertEquals(LoginMethod.GOOGLE, authState.loginMethod)
    }

    @Test
    fun `loginWithSocial with Facebook succeeds`() = runTest(testDispatcher) {
        // When
        val result = repository.loginWithSocial(LoginMethod.FACEBOOK)

        // Then
        assertTrue(result is Result.Success)
        val authState = repository.authStateFlow.first()
        assertTrue(authState is AuthState.Authenticated)
        assertEquals("user@facebook.com", authState.email)
        assertEquals(LoginMethod.FACEBOOK, authState.loginMethod)
    }

    @Test
    fun `loginWithSocial with Apple succeeds`() = runTest(testDispatcher) {
        // When
        val result = repository.loginWithSocial(LoginMethod.APPLE)

        // Then
        assertTrue(result is Result.Success)
        val authState = repository.authStateFlow.first()
        assertTrue(authState is AuthState.Authenticated)
        assertEquals("user@apple.com", authState.email)
        assertEquals(LoginMethod.APPLE, authState.loginMethod)
    }

    @Test
    fun `loginWithSocial with EMAIL method returns error`() = runTest(testDispatcher) {
        // When
        val result = repository.loginWithSocial(LoginMethod.EMAIL)

        // Then
        assertTrue(result is Result.Error)
        assertTrue(result.exception is IllegalArgumentException)
    }

    @Test
    fun `logout clears auth state`() = runTest(testDispatcher) {
        // Given - login first
        repository.loginWithEmail("user@dourobats.com", "123456")
        assertTrue(repository.authStateFlow.first() is AuthState.Authenticated)

        // When
        repository.logout()

        // Then
        val authState = repository.authStateFlow.first()
        assertTrue(authState is AuthState.Unauthenticated)
    }

    @Test
    fun `isAuthenticated returns false initially`() = runTest(testDispatcher) {
        // When
        val isAuth = repository.isAuthenticated()

        // Then
        assertFalse(isAuth)
    }

    @Test
    fun `isAuthenticated returns true after login`() = runTest(testDispatcher) {
        // Given
        repository.loginWithEmail("user@dourobats.com", "123456")

        // When
        val isAuth = repository.isAuthenticated()

        // Then
        assertTrue(isAuth)
    }

    @Test
    fun `isAuthenticated returns false after logout`() = runTest(testDispatcher) {
        // Given - login then logout
        repository.loginWithEmail("user@dourobats.com", "123456")
        repository.logout()

        // When
        val isAuth = repository.isAuthenticated()

        // Then
        assertFalse(isAuth)
    }

    @Test
    fun `authStateFlow emits Unauthenticated initially`() = runTest(testDispatcher) {
        // When
        val authState = repository.authStateFlow.first()

        // Then
        assertTrue(authState is AuthState.Unauthenticated)
    }

    @Test
    fun `authStateFlow emits Authenticated after successful login`() = runTest(testDispatcher) {
        // Given
        repository.loginWithEmail("user@dourobats.com", "123456")

        // When
        val authState = repository.authStateFlow.first()

        // Then
        assertTrue(authState is AuthState.Authenticated)
        assertEquals("user@dourobats.com", authState.email)
    }

    @Test
    fun `authStateFlow persists across repository instances`() = runTest(testDispatcher) {
        // Given - login and create new repository instance
        repository.loginWithEmail("user@dourobats.com", "123456")
        val newRepository = AuthRepositoryImpl(dataStore)

        // When
        val authState = newRepository.authStateFlow.first()

        // Then - should still be authenticated
        assertTrue(authState is AuthState.Authenticated)
        assertEquals("user@dourobats.com", authState.email)
    }

    @Test
    fun `loginWithSocial persists auth state across repository instances`() = runTest(testDispatcher) {
        // Given - social login and create new repository instance
        repository.loginWithSocial(LoginMethod.GOOGLE)
        val newRepository = AuthRepositoryImpl(dataStore)

        // When
        val authState = newRepository.authStateFlow.first()

        // Then - should still be authenticated
        assertTrue(authState is AuthState.Authenticated)
        assertEquals(LoginMethod.GOOGLE, authState.loginMethod)
    }
}
