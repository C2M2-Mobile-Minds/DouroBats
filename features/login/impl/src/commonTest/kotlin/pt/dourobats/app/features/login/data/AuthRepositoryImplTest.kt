package pt.dourobats.app.features.login.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okio.FileSystem
import okio.Path.Companion.toPath
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.api.exception.AuthException
import pt.dourobats.app.features.login.api.model.AuthState
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
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
        try {
            FileSystem.SYSTEM.delete(testFile, mustExist = false)
        } catch (e: Exception) {
            // Ignore
        }
    }

    @Test
    fun `requestLoginCode always returns success`() = runTest(testDispatcher) {
        val result = repository.requestLoginCode("any@test.com")
        assertTrue(result is Result.Success)
    }

    @Test
    fun `requestLoginCode with any email returns success`() = runTest(testDispatcher) {
        val result = repository.requestLoginCode("unknown@example.com")
        assertTrue(result is Result.Success)
    }

    @Test
    fun `verifyLoginCode with correct code returns success`() = runTest(testDispatcher) {
        val result = repository.verifyLoginCode("any@test.com", "123456")
        assertTrue(result is Result.Success)
    }

    @Test
    fun `verifyLoginCode with wrong code returns InvalidCredentials error`() = runTest(testDispatcher) {
        val result = repository.verifyLoginCode("any@test.com", "wrong")
        assertTrue(result is Result.Error)
        assertTrue(result.exception is AuthException.InvalidCredentials)
    }

    @Test
    fun `verifyLoginCode with correct code persists auth state`() = runTest(testDispatcher) {
        repository.verifyLoginCode("any@test.com", "123456")
        advanceUntilIdle()

        val authState = repository.authStateFlow.first()
        assertTrue(authState is AuthState.Authenticated)
        assertEquals("mock_user_001", authState.userId)
        assertEquals("any@test.com", authState.email)
    }

    @Test
    fun `verifyLoginCode with wrong code does not persist auth state`() = runTest(testDispatcher) {
        repository.verifyLoginCode("any@test.com", "wrong")
        advanceUntilIdle()

        val authState = repository.authStateFlow.first()
        assertTrue(authState is AuthState.Unauthenticated)
    }

    @Test
    fun `initial auth state is Unauthenticated`() = runTest(testDispatcher) {
        val authState = repository.authStateFlow.first()
        assertTrue(authState is AuthState.Unauthenticated)
    }

    @Test
    fun `logout clears auth state`() = runTest(testDispatcher) {
        repository.verifyLoginCode("any@test.com", "123456")
        advanceUntilIdle()
        assertTrue(repository.authStateFlow.first() is AuthState.Authenticated)

        repository.logout()
        advanceUntilIdle()
        assertTrue(repository.authStateFlow.first() is AuthState.Unauthenticated)
    }

    @Test
    fun `isAuthenticated returns false when not logged in`() = runTest(testDispatcher) {
        assertFalse(repository.isAuthenticated())
    }

    @Test
    fun `isAuthenticated returns true after verifyLoginCode with correct code`() = runTest(testDispatcher) {
        repository.verifyLoginCode("any@test.com", "123456")
        advanceUntilIdle()
        assertTrue(repository.isAuthenticated())
    }

    @Test
    fun `isAuthenticated returns false after logout`() = runTest(testDispatcher) {
        repository.verifyLoginCode("any@test.com", "123456")
        advanceUntilIdle()
        repository.logout()
        advanceUntilIdle()
        assertFalse(repository.isAuthenticated())
    }
}
