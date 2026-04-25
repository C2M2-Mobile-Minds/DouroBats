package pt.dourobats.app.features.schedule.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import pt.dourobats.app.features.schedule.api.usecase.GetAllSessionsUseCase
import pt.dourobats.app.features.schedule.api.usecase.GetAvailableSessionsUseCase
import pt.dourobats.app.features.schedule.api.usecase.GetUserBookedSessionsUseCase
import pt.dourobats.app.features.schedule.api.usecase.BookSessionUseCase
import pt.dourobats.app.features.schedule.api.usecase.CancelBookingUseCase
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.schedule.calendar.YearMonth
import pt.dourobats.app.features.schedule.ui.mapper.SessionUiMapper
import kotlin.time.Clock

/**
 * ViewModel for the schedule screen.
 */
internal class ScheduleViewModel(
    private val getAvailableSessionsUseCase: GetAvailableSessionsUseCase,
    private val getUserBookedSessionsUseCase: GetUserBookedSessionsUseCase,
    private val getAllSessionsUseCase: GetAllSessionsUseCase,
    private val bookSessionUseCase: BookSessionUseCase,
    private val cancelBookingUseCase: CancelBookingUseCase,
    private val mapper: SessionUiMapper
) : ViewModel() {

    private val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    private val _selectedDate = MutableStateFlow(today)

    private val _uiState = MutableStateFlow(
        ScheduleUiState(
            selectedDate = _selectedDate.value,
            currentYearMonth = YearMonth(today.year, today.month)
        )
    )
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    init {
        loadSessions()
    }

    private fun loadSessions() {
        viewModelScope.launch {
            combine(
                _selectedDate.flatMapLatest { date -> getAvailableSessionsUseCase(date) },
                getUserBookedSessionsUseCase(today),
                getAllSessionsUseCase()
            ) { sessionsForDate, bookedSessions, allSessions ->
                Triple(sessionsForDate, bookedSessions, allSessions)
            }
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "An error occurred"
                        )
                    }
                }
                .collect { (sessionsForDate, bookedSessions, allSessions) ->
                    // Since mapper.map is now suspend, we map the lists in the coroutine context
                    val mappedSessionsForDate = sessionsForDate.map { (session, isBooked) ->
                        mapper.map(session, isBooked)
                    }
                    val mappedBookedSessions = bookedSessions.map { session ->
                        mapper.map(session, true)
                    }

                    _uiState.update {
                        it.copy(
                            sessionsForSelectedDate = mappedSessionsForDate,
                            upcomingBookedSessions = mappedBookedSessions,
                            isLoading = false,
                            errorMessage = null,
                            allSessionDates = allSessions.map { s -> s.dateTime.date }.toSet()
                        )
                    }
                }
        }
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        _uiState.update { 
            it.copy(
                selectedDate = date,
                currentYearMonth = YearMonth(date.year, date.month)
            ) 
        }
    }

    fun updateYearMonth(yearMonth: YearMonth) {
        _uiState.update { it.copy(currentYearMonth = yearMonth) }
    }

    fun toggleViewMode() {
        _uiState.update {
            it.copy(
                viewMode = if (it.viewMode == CalendarViewMode.WEEK) {
                    CalendarViewMode.MONTH
                } else {
                    CalendarViewMode.WEEK
                }
            )
        }
    }

    fun bookSession(sessionId: String) {
        _uiState.update {
            it.copy(
                sessionLoadingStates = it.sessionLoadingStates + (sessionId to true),
                errorMessage = null,
                successMessage = null
            )
        }

        viewModelScope.launch {
            val result = bookSessionUseCase(sessionId)
            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            sessionLoadingStates = it.sessionLoadingStates - sessionId,
                            successMessage = "Session booked successfully"
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            sessionLoadingStates = it.sessionLoadingStates - sessionId,
                            errorMessage = result.message ?: "Failed to book session"
                        )
                    }
                }
                is Result.Loading -> {}
            }
        }
    }

    fun cancelBooking(sessionId: String) {
        _uiState.update {
            it.copy(
                sessionLoadingStates = it.sessionLoadingStates + (sessionId to true),
                errorMessage = null,
                successMessage = null
            )
        }

        viewModelScope.launch {
            val result = cancelBookingUseCase(sessionId)
            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            sessionLoadingStates = it.sessionLoadingStates - sessionId,
                            successMessage = "Booking cancelled successfully"
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            sessionLoadingStates = it.sessionLoadingStates - sessionId,
                            errorMessage = result.message ?: "Failed to cancel booking"
                        )
                    }
                }
                is Result.Loading -> {}
            }
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(
                errorMessage = null,
                successMessage = null
            )
        }
    }
}
