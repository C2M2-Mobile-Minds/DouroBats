package pt.dourobats.app.features.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import pt.dourobats.app.core.model.Session
import pt.dourobats.app.core.domain.usecase.GetAvailableSessionsUseCase
import pt.dourobats.app.core.domain.usecase.GetUserBookedSessionsUseCase
import pt.dourobats.app.core.domain.usecase.BookSessionUseCase
import pt.dourobats.app.core.domain.usecase.CancelBookingUseCase
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.core.ui.model.SessionDisplayData
import kotlin.time.Clock

/**
 * ViewModel for the schedule screen.
 * Manages UI state and business logic using use cases.
 */
class ScheduleViewModel(
    private val getAvailableSessionsUseCase: GetAvailableSessionsUseCase,
    private val getUserBookedSessionsUseCase: GetUserBookedSessionsUseCase,
    private val bookSessionUseCase: BookSessionUseCase,
    private val cancelBookingUseCase: CancelBookingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ScheduleUiState(
            selectedDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        )
    )
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    init {
        loadSessions()
    }

    /**
     * Load sessions for selected date and user's booked sessions.
     * Combines both flows to update UI state reactively.
     */
    private fun loadSessions() {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

        viewModelScope.launch {
            combine(
                getAvailableSessionsUseCase(_uiState.value.selectedDate),
                getUserBookedSessionsUseCase(today)
            ) { sessionsForDate, bookedSessions ->
                Triple(
                    sessionsForDate.map { (session, isBooked) ->
                        session.toDisplayData(isBooked)
                    },
                    bookedSessions.map { it.toDisplayData(true) },
                    false // isLoading
                )
            }
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "An error occurred"
                        )
                    }
                }
                .collect { (sessionsForDate, bookedSessions, isLoading) ->
                    _uiState.update {
                        it.copy(
                            sessionsForSelectedDate = sessionsForDate,
                            upcomingBookedSessions = bookedSessions,
                            isLoading = isLoading,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    /**
     * Update the selected date and reload sessions for that date.
     */
    fun selectDate(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
        loadSessionsForDate(date)
    }

    /**
     * Toggle between week and month calendar view.
     */
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

    /**
     * Load sessions for a specific date.
     */
    private fun loadSessionsForDate(date: LocalDate) {
        viewModelScope.launch {
            getAvailableSessionsUseCase(date)
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            errorMessage = error.message ?: "Failed to load sessions"
                        )
                    }
                }
                .collect { sessions ->
                    _uiState.update {
                        it.copy(
                            sessionsForSelectedDate = sessions.map { (session, isBooked) ->
                                session.toDisplayData(isBooked)
                            }
                        )
                    }
                }
        }
    }

    /**
     * Convert domain Session to UI SessionDisplayData.
     * Maps sport IDs to display names and icons.
     */
    private fun Session.toDisplayData(isBooked: Boolean): SessionDisplayData {
        return SessionDisplayData(
            session = this,
            sportName = when (sportId) {
                "volleyball" -> "Volleyball"
                "futsal" -> "Futsal"
                else -> "Unknown"
            },
            sportIcon = when (sportId) {
                "volleyball" -> "🏐"
                "futsal" -> "⚽"
                else -> "🏃"
            },
            venueName = venueId, // In real app, map venue ID to venue name
            isUserBooked = isBooked
        )
    }

    /**
     * Books a session for the current user.
     * Sets loading state for the specific session during the operation.
     *
     * @param sessionId ID of the session to book
     */
    fun bookSession(sessionId: String) {
        // Set loading state for this specific session
        _uiState.update {
            it.copy(
                sessionLoadingStates = it.sessionLoadingStates + (sessionId to true),
                errorMessage = null,
                successMessage = null
            )
        }

        viewModelScope.launch {
            // Execute use case
            val result = bookSessionUseCase(sessionId)

            // Handle result
            when (result) {
                is Result.Success -> {
                    // Clear loading state and show success
                    _uiState.update {
                        it.copy(
                            sessionLoadingStates = it.sessionLoadingStates - sessionId,
                            successMessage = "Session booked successfully"
                        )
                    }
                    // Reload sessions to get updated data
                    loadSessions()
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            sessionLoadingStates = it.sessionLoadingStates - sessionId,
                            errorMessage = result.message ?: "Failed to book session"
                        )
                    }
                }
                is Result.Loading -> {
                    // Already in loading state
                }
            }
        }
    }

    /**
     * Cancels a booked session for the current user.
     * Sets loading state for the specific session during the operation.
     *
     * @param sessionId ID of the session to cancel
     */
    fun cancelBooking(sessionId: String) {
        // Set loading state for this specific session
        _uiState.update {
            it.copy(
                sessionLoadingStates = it.sessionLoadingStates + (sessionId to true),
                errorMessage = null,
                successMessage = null
            )
        }

        viewModelScope.launch {
            // Execute use case
            val result = cancelBookingUseCase(sessionId)

            // Handle result
            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            sessionLoadingStates = it.sessionLoadingStates - sessionId,
                            successMessage = "Booking cancelled successfully"
                        )
                    }
                    // Reload sessions to get updated data
                    loadSessions()
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            sessionLoadingStates = it.sessionLoadingStates - sessionId,
                            errorMessage = result.message ?: "Failed to cancel booking"
                        )
                    }
                }
                is Result.Loading -> {
                    // Already in loading state
                }
            }
        }
    }

    /**
     * Clears success and error messages.
     * Called when user dismisses feedback or navigates away.
     */
    fun clearMessages() {
        _uiState.update {
            it.copy(
                errorMessage = null,
                successMessage = null
            )
        }
    }
}
