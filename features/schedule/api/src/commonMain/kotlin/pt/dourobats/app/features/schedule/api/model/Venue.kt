package pt.dourobats.app.features.schedule.api.model

/**
 * Represents a physical venue where training sessions take place.
 *
 * @property id Unique identifier for the venue.
 * @property name Human-readable venue name (e.g., "Campo Principal").
 * @property address Physical address of the venue.
 * @property capacity Maximum number of simultaneous attendees the venue supports.
 * @property supportedSports List of sports that can be practised at this venue.
 */
data class Venue(
    val id: String,
    val name: String,
    val address: String,
    val capacity: Int,
    val supportedSports: List<Sport> = emptyList()
)
