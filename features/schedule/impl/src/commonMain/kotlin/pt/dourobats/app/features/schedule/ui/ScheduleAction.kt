package pt.dourobats.app.features.schedule.ui

import kotlinx.datetime.LocalDate

internal sealed interface ScheduleAction {
    data object ToggleViewMode : ScheduleAction
    data class SelectDate(val date: LocalDate) : ScheduleAction
    data class BookSession(val sessionId: String) : ScheduleAction
    data class CancelBooking(val sessionId: String) : ScheduleAction
}
