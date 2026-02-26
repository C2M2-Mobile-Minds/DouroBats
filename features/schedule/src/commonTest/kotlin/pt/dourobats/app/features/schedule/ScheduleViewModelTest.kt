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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import pt.dourobats.app.core.model.Session
import pt.dourobats.app.core.model.SessionStatus
import pt.dourobats.app.core.model.SkillLevel
import pt.dourobats.app.core.domain.usecase.GetAvailableSessionsUseCase
import pt.dourobats.app.core.domain.usecase.GetUserBookedSessionsUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours
import pt.dourobats.app.core.common.Result
import kotlin.time.Clock

@OptIn(ExperimentalCoroutinesApi::class)
class ScheduleViewModelTest {

    private lateinit var viewModel: ScheduleViewModel
    private lateinit var fakeGetAvailableSessionsUseCase: pt.dourobats.app.core.test.fakes.FakeGetAvailableSessionsUseCase
    private lateinit var fakeGetUserBookedSessionsUseCase: pt.dourobats.app.core.test.fakes.FakeGetUserBookedSessionsUseCase
    private lateinit var fakeBookSessionUseCase: pt.dourobats.app.core.test.fakes.FakeBookSessionUseCase
    private lateinit var fakeCancelBookingUseCase: pt.dourobats.app.core.test.fakes.FakeCancelBookingUseCase
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeGetAvailableSessionsUseCase = pt.dourobats.app.core.test.fakes.FakeGetAvailableSessionsUseCase()
        fakeGetUserBookedSessionsUseCase = pt.dourobats.app.core.test.fakes.FakeGetUserBookedSessionsUseCase()
        fakeBookSessionUseCase = pt.dourobats.app.core.test.fakes.FakeBookSessionUseCase()
        fakeCancelBookingUseCase = pt.dourobats.app.core.test.fakes.FakeCancelBookingUseCase()
    }

    private fun createViewModel() {
        viewModel = ScheduleViewModel(
            getAvailableSessionsUseCase = fakeGetAvailableSessionsUseCase,
            getUserBookedSessionsUseCase = fakeGetUserBookedSessionsUseCase,
            bookSessionUseCase = fakeBookSessionUseCase,
            cancelBookingUseCase = fakeCancelBookingUseCase
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state shows loading`() {
        createViewModel()
        val state = viewModel.uiState.value
        assertTrue(state.isLoading)
    }

    @Test
    fun `loads sessions successfully`() = runTest {
        // Given
        val date = LocalDate(2026, 1, 12)
        val session = createTestSession("1", date)
        fakeGetAvailableSessionsUseCase.sessions = listOf(session to false)
        fakeGetUserBookedSessionsUseCase.sessions = emptyList()

        // When
        createViewModel()
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
        createViewModel()
        advanceUntilIdle()
        val newDate = LocalDate(2026, 1, 13)
        val session = createTestSession("2", newDate)
        fakeGetAvailableSessionsUseCase.sessions = listOf(session to false)

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
        createViewModel()
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
        // Given - use today's date since ViewModel uses today for booked sessions
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val bookedSession = createTestSession("3", today)
        fakeGetAvailableSessionsUseCase.sessions = emptyList()
        fakeGetUserBookedSessionsUseCase.sessions = listOf(bookedSession)

        // When
        createViewModel()
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
