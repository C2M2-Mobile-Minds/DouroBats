package pt.dourobats.app.features.management.ui

import pt.dourobats.app.features.venues.api.model.Venue

internal data class CreateSessionUiState(
    val sessionType: SessionType = SessionType.PRACTICE,
    val sessionName: String = "",
    val selectedVenue: Venue? = null,
    val availableVenues: List<Venue> = emptyList(),
    val maxAthletes: String = "",
    val selectedDate: String = "",
    val selectedTime: String = "",
    val showErrors: Boolean = false,
    val nameError: Boolean = false,
    val venueError: Boolean = false,
    val dateError: Boolean = false,
    val timeError: Boolean = false,
    val isLoading: Boolean = false,
    val isVenuesLoading: Boolean = false,
) {
    val isFormValid: Boolean
        get() = sessionName.isNotBlank() && selectedVenue != null && selectedDate.isNotBlank() && selectedTime.isNotBlank()
}
