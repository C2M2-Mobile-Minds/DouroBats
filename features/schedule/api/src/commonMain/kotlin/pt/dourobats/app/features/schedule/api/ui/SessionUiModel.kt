package pt.dourobats.app.features.schedule.api.ui

import pt.dourobats.app.features.schedule.api.model.Session

/**
 * UI model for a session, containing all the information needed for display.
 * Maps domain [Session] data to presentation-friendly strings and icons.
 */
data class SessionUiModel(
    val session: Session,
    val sportName: String,
    val sportIcon: String,
    val venueName: String,
    val isUserBooked: Boolean,
    val formattedTimeRange: String,
    val formattedShortDate: String
)
