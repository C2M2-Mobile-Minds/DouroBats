package pt.dourobats.app.features.venues.api.model

/**
 * Domain model representing a training location.
 *
 * @param id Unique identifier for the venue
 * @param name Human-readable name (e.g., "Pavilhão Municipal")
 * @param address Physical address of the venue
 * @param capacity Informational capacity of the venue
 * @param sportIds List of sport IDs supported by this venue (e.g., ["volleyball", "basketball"])
 */
data class Venue(
    val id: String,
    val name: String,
    val address: String,
    val capacity: Int,
    val sportIds: List<String>
)
