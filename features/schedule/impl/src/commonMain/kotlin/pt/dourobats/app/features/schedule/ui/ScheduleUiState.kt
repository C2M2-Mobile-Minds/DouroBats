package pt.dourobats.app.features.schedule.ui

import kotlinx.datetime.LocalDate
import pt.dourobats.app.features.schedule.api.ui.SessionUiModel
import pt.dourobats.app.features.schedule.calendar.YearMonth

/**
 * UI state for the schedule screen.
 * Immutable data class representing the current state of the screen.
 */
internal data class ScheduleUiState(
    val selectedDate: LocalDate,
    val currentYearMonth: YearMonth,
    val viewMode: CalendarViewMode = CalendarViewMode.WEEK,
    val sessionsForSelectedDate: List<SessionUiModel> = emptyList(),
    val upcomingBookedSessions: List<SessionUiModel> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val sessionLoadingStates: Map<String, Boolean> = emptyMap(),
    val successMessage: String? = null,
    val allSessionDates: Set<LocalDate> = emptySet()
)

/**
 * Calendar view mode enum.
 */
internal enum class CalendarViewMode {
    WEEK,
    MONTH
}
