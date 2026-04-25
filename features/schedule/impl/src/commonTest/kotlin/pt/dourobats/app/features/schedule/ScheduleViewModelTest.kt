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
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.venues.api.model.Venue
import pt.dourobats.app.features.venues.api.usecase.GetVenueByIdUseCase
import pt.dourobats.app.features.schedule.api.model.Session
import pt.dourobats.app.features.schedule.api.model.SessionStatus
import pt.dourobats.app.features.schedule.api.model.SkillLevel
import pt.dourobats.app.features.schedule.calendar.YearMonth
import pt.dourobats.app.features.schedule.testing.FakeBookSessionUseCase
import pt.dourobats.app.features.schedule.testing.FakeCancelBookingUseCase
import pt.dourobats.app.features.schedule.testing.FakeGetAllSessionsUseCase
import pt.dourobats.app.features.schedule.testing.FakeGetAvailableSessionsUseCase
import pt.dourobats.app.features.schedule.testing.FakeGetUserBookedSessionsUseCase
import pt.dourobats.app.features.schedule.ui.CalendarViewMode
import pt.dourobats.app.features.schedule.ui.ScheduleViewModel
import pt.dourobats.app.features.schedule.ui.mapper.SessionUiMapper
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
    private val fakeGetVenueById = object : GetVenueByIdUseCase {
        override suspend fun invoke(id: String) = Result.Success(Venue(id = id, name = id, address = "", capacity = 0, sportIds = emptyList()))
    }
    private val mapper = SessionUiMapper(fakeGetVenueById)

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
            cancelBookingUseCase = fakeCancelBookingUseCase.build(),
            mapper = mapper
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state shows loading and current month`() {
        createViewModel()
        val state = viewModel.uiState.value
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        
        assertTrue(state.isLoading)
        assertEquals(YearMonth(today.year, today.month), state.currentYearMonth)
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
    fun `selectDate updates selected date and year month`() = runTest {
        createViewModel()
        advanceUntilIdle()
        val newDate = LocalDate(2026, 5, 20)
        
        viewModel.selectDate(newDate)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(newDate, state.selectedDate)
        assertEquals(YearMonth(2026, Month.MAY), state.currentYearMonth)
    }

    @Test
    fun `updateYearMonth updates current year month without changing selected date`() = runTest {
        val initialDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        createViewModel()
        advanceUntilIdle()
        
        val targetMonth = YearMonth(2027, Month.DECEMBER)
        viewModel.updateYearMonth(targetMonth)
        
        val state = viewModel.uiState.value
        assertEquals(targetMonth, state.currentYearMonth)
        assertEquals(initialDate, state.selectedDate)
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
