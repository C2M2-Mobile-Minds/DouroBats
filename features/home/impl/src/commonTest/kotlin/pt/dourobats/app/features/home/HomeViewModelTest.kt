package pt.dourobats.app.features.home

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import pt.dourobats.app.features.login.api.UserProfile
import pt.dourobats.app.features.login.api.UserRole
import pt.dourobats.app.features.settings.testing.FakeObserveUserProfileUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var observeUserProfile: FakeObserveUserProfileUseCase
    private lateinit var viewModel: HomeViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        observeUserProfile = FakeObserveUserProfileUseCase()
        viewModel = HomeViewModel(observeUserProfile)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `isCommitteeUser is false by default`() = runTest(testDispatcher) {
        val collectorJob = launch { viewModel.uiState.collect {} }
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isCommitteeUser)
        collectorJob.cancel()
    }

    @Test
    fun `isCommitteeUser is false for user with only ATHLETE role`() = runTest(testDispatcher) {
        val profile = UserProfile.empty().copy(roles = listOf(UserRole.ATHLETE))
        observeUserProfile.profileFlow.value = profile

        val collectorJob = launch { viewModel.uiState.collect {} }
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isCommitteeUser)
        collectorJob.cancel()
    }

    @Test
    fun `isCommitteeUser is false for SUPPORTER role`() = runTest(testDispatcher) {
        val profile = UserProfile.empty().copy(roles = listOf(UserRole.SUPPORTER))
        observeUserProfile.profileFlow.value = profile

        val collectorJob = launch { viewModel.uiState.collect {} }
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isCommitteeUser)
        collectorJob.cancel()
    }

    @Test
    fun `isCommitteeUser is true when user has COMMITTEE role`() = runTest(testDispatcher) {
        val profile = UserProfile.empty().copy(roles = listOf(UserRole.COMMITTEE))
        observeUserProfile.profileFlow.value = profile

        val collectorJob = launch { viewModel.uiState.collect {} }
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isCommitteeUser)
        collectorJob.cancel()
    }

    @Test
    fun `isCommitteeUser is true when user has COMMITTEE among multiple roles`() = runTest(testDispatcher) {
        val profile = UserProfile.empty().copy(roles = listOf(UserRole.ATHLETE, UserRole.COMMITTEE))
        observeUserProfile.profileFlow.value = profile

        val collectorJob = launch { viewModel.uiState.collect {} }
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isCommitteeUser)
        collectorJob.cancel()
    }

    @Test
    fun `isCommitteeUser updates when profile changes`() = runTest(testDispatcher) {
        val collectorJob = launch { viewModel.uiState.collect {} }
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isCommitteeUser)

        observeUserProfile.profileFlow.value = UserProfile.empty().copy(roles = listOf(UserRole.COMMITTEE))
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.isCommitteeUser)

        observeUserProfile.profileFlow.value = UserProfile.empty().copy(roles = listOf(UserRole.ATHLETE))
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isCommitteeUser)

        collectorJob.cancel()
    }
}
