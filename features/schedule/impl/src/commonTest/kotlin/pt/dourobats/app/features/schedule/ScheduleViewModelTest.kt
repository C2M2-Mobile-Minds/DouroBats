package pt.dourobats.app.features.schedule

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import pt.dourobats.app.features.schedule.api.model.Session
import pt.dourobats.app.features.schedule.api.model.SessionStatus
import pt.dourobats.app.features.schedule.api.model.SkillLevel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import pt.dourobats.app.features.schedule.testing.FakeGetAllSessionsUseCase
import pt.dourobats.app.features.schedule.testing.FakeGetAvailableSessionsUseCase
import pt.dourobats.app.features.schedule.testing.FakeGetUserBookedSessionsUseCase
import pt.dourobats.app.features.schedule.testing.FakeBookSessionUseCase
import pt.dourobats.app.features.schedule.testing.FakeCancelBookingUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class ScheduleViewModelTest {

    private lateinit var viewModel: ScheduleViewModel
    private lateinit var fakeGetAvailableSessionsUseCase: FakeGetAvailableSessionsUseCase
    private lateinit var fakeGetUserBookedSessionsUseCase: FakeGetUserBookedSessionsUseCase
    private lateinit var fakeGetAllSessionsUseCase: FakeGetAllSessionsUseCase
    private lateinit var fakeBookSessionUseCase: FakeBookSessionUseCase
    private lateinit var fakeCancelBookingUseCase: FakeCancelBookingUseCase
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeGetAvailableSessionsUseCase = FakeGetAvailableSessionsUseCase()
        fakeGetUserBookedSessionsUseCase = FakeGetUserBookedSessionsUseCase()
        fakeGetAllSessionsUseCase = FakeGetAllSessionsUseCase()
        fakeBookSessionUseCase = FakeBookSessionUseCase()
        fakeCancelBookingUseCase = FakeCancelBookingUseCase()
    }

    private fun createViewModel() {
        viewModel = ScheduleViewModel(
            getAvailableSessionsUseCase = fakeGetAvailableSessionsUseCase,
            getUserBookedSessionsUseCase = fakeGetUserBookedSessionsUseCase,
            getAllSessionsUseCase = fakeGetAllSessionsUseCase,
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
