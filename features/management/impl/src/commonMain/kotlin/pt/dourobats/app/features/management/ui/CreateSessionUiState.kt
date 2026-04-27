package pt.dourobats.app.features.management.ui

import pt.dourobats.app.features.venues.api.model.Venue

internal data class CreateSessionUiState(
    val sessionType: SessionType = SessionType.PRACTICE,
    val sessionName: String = "",
    val selectedVenue: Venue? = null,
    val availableVenues: List<Venue> = emptyList(),
    val selectedLevel: SessionLevel? = null,
    val coachName: String = "",
    val duration: Int? = null,
    val maxAthletes: String = "",
    val selectedDate: String = "",
    val selectedTime: String = "",
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val templates: List<SessionTemplate> = emptyList(),
    val shouldSaveAsTemplate: Boolean = false,
    val templateName: String = "",
    val observations: String = "",
    val showErrors: Boolean = false,
    val nameError: Boolean = false,
    val venueError: Boolean = false,
    val levelError: Boolean = false,
    val durationError: Boolean = false,
    val dateError: Boolean = false,
    val timeError: Boolean = false,
    val templateNameError: Boolean = false,
    val isLoading: Boolean = false,
    val isVenuesLoading: Boolean = false,
) {
    val isFormValid: Boolean
        get() = sessionName.isNotBlank() && selectedVenue != null && selectedDate.isNotBlank() && selectedTime.isNotBlank()
}
