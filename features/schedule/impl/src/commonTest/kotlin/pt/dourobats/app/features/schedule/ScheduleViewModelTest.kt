package pt.dourobats.app.features.schedule

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.schedule.api.model.Session
import pt.dourobats.app.features.schedule.api.model.SessionStatus
import pt.dourobats.app.features.schedule.api.model.SkillLevel
import pt.dourobats.app.features.schedule.testing.FakeBookSessionUseCase
import pt.dourobats.app.features.schedule.testing.FakeCancelBookingUseCase
import pt.dourobats.app.features.schedule.testing.FakeGetAllSessionsUseCase
import pt.dourobats.app.features.schedule.testing.FakeGetAvailableSessionsUseCase
import pt.dourobats.app.features.schedule.testing.FakeGetUserBookedSessionsUseCase
import pt.dourobats.app.features.schedule.ui.CalendarViewMode
import pt.dourobats.app.features.schedule.ui.ScheduleViewModel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours

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
        fakeGetAvailableSessionsUseCase = FakeGetAvailableSessionsUseCase().apply {
            invoke = { _ -> flowOf(emptyList()) }
        }
        fakeGetUserBookedSessionsUseCase = FakeGetUserBookedSessionsUseCase().apply {
            invoke = { _ -> flowOf(emptyList()) }
        }
        fakeGetAllSessionsUseCase = FakeGetAllSessionsUseCase().apply {
            invoke = { flowOf(emptyList()) }
        }
        fakeBookSessionUseCase = FakeBookSessionUseCase().apply {
            invoke = { _ -> Result.Success(Unit) }
        }
        fakeCancelBookingUseCase = FakeCancelBookingUseCase().apply {
            invoke = { _ -> Result.Success(Unit) }
        }
    }

    private fun createViewModel() {
        viewModel = ScheduleViewModel(
            getAvailableSessionsUseCase = fakeGetAvailableSessionsUseCase.build(),
            getUserBookedSessionsUseCase = fakeGetUserBookedSessionsUseCase.build(),
            getAllSessionsUseCase = fakeGetAllSessionsUseCase.build(),
            bookSessionUseCase = fakeBookSessionUseCase.build(),
            cancelBookingUseCase = fakeCancelBookingUseCase.build()
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
        val date = LocalDate(2026, 1, 12)
        val session = createTestSession("1", date)
        fakeGetAvailableSessionsUseCase.invoke = { _ -> flowOf(listOf(session to false)) }
        fakeGetUserBookedSessionsUseCase.invoke = { _ -> flowOf(emptyList()) }

        createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(1, state.sessionsForSelectedDate.size)
        assertEquals("1", state.sessionsForSelectedDate.first().session.id)
    }

    @Test
    fun `selectDate updates selected date and loads new sessions`() = runTest {
        createViewModel()
        advanceUntilIdle()
        val newDate = LocalDate(2026, 1, 13)
        val session = createTestSession("2", newDate)
        fakeGetAvailableSessionsUseCase.invoke = { _ -> flowOf(listOf(session to false)) }

        viewModel.selectDate(newDate)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(newDate, state.selectedDate)
        assertEquals(1, state.sessionsForSelectedDate.size)
        assertEquals("2", state.sessionsForSelectedDate.first().session.id)
    }

    @Test
    fun `toggleViewMode switches between week and month`() = runTest {
        createViewModel()
        advanceUntilIdle()
        val initialViewMode = viewModel.uiState.value.viewMode

        viewModel.toggleViewMode()

        val newViewMode = viewModel.uiState.value.viewMode
        assertEquals(
            if (initialViewMode == CalendarViewMode.WEEK) CalendarViewMode.MONTH else CalendarViewMode.WEEK,
            newViewMode
        )
    }

    @Test
    fun `loads user booked sessions`() = runTest {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val bookedSession = createTestSession("3", today)
        fakeGetAvailableSessionsUseCase.invoke = { _ -> flowOf(emptyList()) }
        fakeGetUserBookedSessionsUseCase.invoke = { _ -> flowOf(listOf(bookedSession)) }

        createViewModel()
        advanceUntilIdle()

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
