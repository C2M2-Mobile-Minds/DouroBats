package pt.dourobats.app.features.schedule.api.ui

import pt.dourobats.app.features.schedule.api.model.Session

/**
 * Extended session model with display data for UI components.
 *
 * Combines Session domain model with presentation-specific information
 * like sport names, icons, venue names, and booking status.
 *
 * @param session The underlying domain model
 * @param sportName Human-readable sport name (e.g., "Baseball")
 * @param sportIcon Emoji or icon representing the sport (e.g., "⚾")
 * @param venueName Human-readable venue name
 * @param isUserBooked Whether the current user has booked this session
 */
data class SessionDisplayData(
    val session: Session,
    val sportName: String,
    val sportIcon: String,
    val venueName: String,
    val isUserBooked: Boolean
)
