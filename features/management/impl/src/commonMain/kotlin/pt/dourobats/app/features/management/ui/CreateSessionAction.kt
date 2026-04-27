package pt.dourobats.app.features.management.ui

import pt.dourobats.app.features.venues.api.model.Venue

internal sealed interface CreateSessionAction {
    data class UpdateName(val name: String) : CreateSessionAction
    data class UpdateVenue(val venue: Venue) : CreateSessionAction
    data class UpdateMaxAthletes(val value: String) : CreateSessionAction
    data class UpdateSessionType(val type: SessionType) : CreateSessionAction
    data class SelectDate(val date: String) : CreateSessionAction
    data class SelectTime(val time: String) : CreateSessionAction
    data object Submit : CreateSessionAction
    data object NavigateBack : CreateSessionAction
}
