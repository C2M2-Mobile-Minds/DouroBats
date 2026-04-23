package pt.dourobats.app.features.home

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
import pt.dourobats.app.features.login.api.model.UserRole
import pt.dourobats.app.features.home.ui.HomeViewModel
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

    private val profileFlow = MutableStateFlow(UserProfile.empty())

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        profileFlow.value = UserProfile.empty()
        observeUserProfile = FakeObserveUserProfileUseCase().apply { invoke = { profileFlow } }
        viewModel = HomeViewModel(observeUserProfile.build())
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
        profileFlow.value = profile

        val collectorJob = launch { viewModel.uiState.collect {} }
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isCommitteeUser)
        collectorJob.cancel()
    }

    @Test
    fun `isCommitteeUser is false for SUPPORTER role`() = runTest(testDispatcher) {
        val profile = UserProfile.empty().copy(roles = listOf(UserRole.SUPPORTER))
        profileFlow.value = profile

        val collectorJob = launch { viewModel.uiState.collect {} }
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isCommitteeUser)
        collectorJob.cancel()
    }

    @Test
    fun `isCommitteeUser is true when user has COMMITTEE role`() = runTest(testDispatcher) {
        val profile = UserProfile.empty().copy(roles = listOf(UserRole.COMMITTEE))
        profileFlow.value = profile

        val collectorJob = launch { viewModel.uiState.collect {} }
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isCommitteeUser)
        collectorJob.cancel()
    }

    @Test
    fun `isCommitteeUser is true when user has COMMITTEE among multiple roles`() = runTest(testDispatcher) {
        val profile = UserProfile.empty().copy(roles = listOf(UserRole.ATHLETE, UserRole.COMMITTEE))
        profileFlow.value = profile

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

        profileFlow.value = UserProfile.empty().copy(roles = listOf(UserRole.COMMITTEE))
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.isCommitteeUser)

        profileFlow.value = UserProfile.empty().copy(roles = listOf(UserRole.ATHLETE))
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isCommitteeUser)

        collectorJob.cancel()
    }
}
