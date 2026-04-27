package pt.dourobats.app.features.management.ui

import pt.dourobats.app.features.venues.api.model.Venue

internal sealed interface CreateSessionAction {
    data class UpdateName(val name: String) : CreateSessionAction
    data class UpdateVenue(val venue: Venue) : CreateSessionAction
    data class UpdateMaxAthletes(val value: String) : CreateSessionAction
    data class UpdateSessionType(val type: SessionType) : CreateSessionAction
    data class UpdateLevel(val level: SessionLevel) : CreateSessionAction
    data class UpdateCoach(val coach: String) : CreateSessionAction
    data class UpdateDuration(val minutes: Int) : CreateSessionAction
    data class SelectDate(val date: String) : CreateSessionAction
    data class SelectTime(val time: String) : CreateSessionAction
    data class UpdateObservations(val value: String) : CreateSessionAction
    data class SelectTemplate(val template: SessionTemplate) : CreateSessionAction
    data class ToggleSaveTemplate(val enabled: Boolean) : CreateSessionAction
    data class UpdateTemplateName(val name: String) : CreateSessionAction
    data object OpenDatePicker : CreateSessionAction
    data object OpenTimePicker : CreateSessionAction
    data object ClosePickers : CreateSessionAction
    data object Submit : CreateSessionAction
    data object NavigateBack : CreateSessionAction
}
