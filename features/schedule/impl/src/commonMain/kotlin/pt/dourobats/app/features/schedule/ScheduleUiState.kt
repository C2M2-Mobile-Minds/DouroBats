package pt.dourobats.app.features.schedule

import kotlinx.datetime.LocalDate
import pt.dourobats.app.core.ui.model.SessionDisplayData

/**
 * UI state for the schedule screen.
 * Immutable data class representing the current state of the screen.
 */
internal data class ScheduleUiState(
    val selectedDate: LocalDate,
    val viewMode: CalendarViewMode = CalendarViewMode.WEEK,
    val sessionsForSelectedDate: List<SessionDisplayData> = emptyList(),
    val upcomingBookedSessions: List<SessionDisplayData> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val sessionLoadingStates: Map<String, Boolean> = emptyMap(),
    val successMessage: String? = null,
    val allSessionDates: Set<LocalDate> = emptySet()
)

/**
 * Calendar view mode enum.
 */
enum class CalendarViewMode {
    WEEK,
    MONTH
}
