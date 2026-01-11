package pt.dourobats.app.features.schedule

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import pt.dourobats.app.core.domain.model.Session
import pt.dourobats.app.core.domain.model.SessionStatus
import pt.dourobats.app.core.domain.model.SkillLevel
import pt.dourobats.app.core.domain.usecase.GetAvailableSessionsUseCase
import pt.dourobats.app.core.domain.usecase.GetUserBookedSessionsUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours

@OptIn(ExperimentalCoroutinesApi::class)
class ScheduleViewModelTest {

    private lateinit var viewModel: ScheduleViewModel
    private lateinit var fakeGetAvailableSessionsUseCase: FakeGetAvailableSessionsUseCase
    private lateinit var fakeGetUserBookedSessionsUseCase: FakeGetUserBookedSessionsUseCase
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeGetAvailableSessionsUseCase = FakeGetAvailableSessionsUseCase()
        fakeGetUserBookedSessionsUseCase = FakeGetUserBookedSessionsUseCase()
        viewModel = ScheduleViewModel(
            getAvailableSessionsUseCase = fakeGetAvailableSessionsUseCase,
            getUserBookedSessionsUseCase = fakeGetUserBookedSessionsUseCase
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state shows loading`() {
        val state = viewModel.uiState.value
        assertTrue(state.isLoading)
    }

    @Test
    fun `loads sessions successfully`() = runTest {
        // Given
        val date = LocalDate(2026, 1, 6)
        val session = createTestSession("1", date)
        fakeGetAvailableSessionsUseCase.sessionsForDate = listOf(session to false)
        fakeGetUserBookedSessionsUseCase.bookedSessions = emptyList()

        // When
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(1, state.sessionsForSelectedDate.size)
        assertEquals("1", state.sessionsForSelectedDate.first().session.id)
    }

    @Test
    fun `selectDate updates selected date and loads new sessions`() = runTest {
        // Given
        advanceUntilIdle()
        val newDate = LocalDate(2026, 1, 7)
        val session = createTestSession("2", newDate)
        fakeGetAvailableSessionsUseCase.sessionsForDate = listOf(session to false)

        // When
        viewModel.selectDate(newDate)
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertEquals(newDate, state.selectedDate)
        assertEquals(1, state.sessionsForSelectedDate.size)
        assertEquals("2", state.sessionsForSelectedDate.first().session.id)
    }

    @Test
    fun `toggleViewMode switches between week and month`() = runTest {
        // Given
        advanceUntilIdle()
        val initialViewMode = viewModel.uiState.value.viewMode

        // When
        viewModel.toggleViewMode()

        // Then
        val newViewMode = viewModel.uiState.value.viewMode
        assertEquals(
            if (initialViewMode == CalendarViewMode.WEEK) CalendarViewMode.MONTH else CalendarViewMode.WEEK,
            newViewMode
        )
    }

    @Test
    fun `loads user booked sessions`() = runTest {
        // Given
        val date = LocalDate(2026, 1, 6)
        val bookedSession = createTestSession("3", date)
        fakeGetAvailableSessionsUseCase.sessionsForDate = emptyList()
        fakeGetUserBookedSessionsUseCase.bookedSessions = listOf(bookedSession)

        // When
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertEquals(1, state.upcomingBookedSessions.size)
        assertEquals("3", state.upcomingBookedSessions.first().session.id)
        assertTrue(state.upcomingBookedSessions.first().isUserBooked)
    }

    private fun createTestSession(id: String, date: LocalDate): Session {
        return Session(
            id = id,
            sportId = "volleyball",
            dateTime = LocalDateTime(date, LocalTime(18, 0)),
            duration = 2.hours,
            venueId = "venue-1",
            targetLevel = SkillLevel.INTERMEDIATE,
            capacity = 20,
            currentAttendees = 10,
            status = SessionStatus.SCHEDULED
        )
    }
}

/**
 * Fake implementation of GetAvailableSessionsUseCase for testing.
 */
private class FakeGetAvailableSessionsUseCase : GetAvailableSessionsUseCase(
    trainingRepository = object : pt.dourobats.app.core.domain.repository.TrainingRepository {
        override fun getSessionsByDate(date: LocalDate) = flowOf(emptyList<Session>())
        override fun getAllSessions() = flowOf(emptyList<Session>())
        override fun getUserBookedSessionIds() = flowOf(emptySet<String>())
        override suspend fun bookSession(sessionId: String) =
            pt.dourobats.app.core.domain.model.Result.Success(Unit)
        override suspend fun cancelBooking(sessionId: String) =
            pt.dourobats.app.core.domain.model.Result.Success(Unit)
        override suspend fun getSessionById(id: String) =
            pt.dourobats.app.core.domain.model.Result.Error(
                pt.dourobats.app.core.domain.exception.NetworkException("Not found")
            )
    }
) {
    var sessionsForDate: List<Pair<Session, Boolean>> = emptyList()

    override operator fun invoke(date: LocalDate): Flow<List<Pair<Session, Boolean>>> {
        return flowOf(sessionsForDate)
    }
}

/**
 * Fake implementation of GetUserBookedSessionsUseCase for testing.
 */
private class FakeGetUserBookedSessionsUseCase : GetUserBookedSessionsUseCase(
    trainingRepository = object : pt.dourobats.app.core.domain.repository.TrainingRepository {
        override fun getSessionsByDate(date: LocalDate) = flowOf(emptyList<Session>())
        override fun getAllSessions() = flowOf(emptyList<Session>())
        override fun getUserBookedSessionIds() = flowOf(emptySet<String>())
        override suspend fun bookSession(sessionId: String) =
            pt.dourobats.app.core.domain.model.Result.Success(Unit)
        override suspend fun cancelBooking(sessionId: String) =
            pt.dourobats.app.core.domain.model.Result.Success(Unit)
        override suspend fun getSessionById(id: String) =
            pt.dourobats.app.core.domain.model.Result.Error(
                pt.dourobats.app.core.domain.exception.NetworkException("Not found")
            )
    }
) {
    var bookedSessions: List<Session> = emptyList()

    override operator fun invoke(fromDate: LocalDate): Flow<List<Session>> {
        return flowOf(bookedSessions)
    }
}
