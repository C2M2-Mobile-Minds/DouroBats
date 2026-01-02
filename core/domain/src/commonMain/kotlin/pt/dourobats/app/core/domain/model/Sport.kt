package pt.dourobats.app.core.domain.model

/**
 * Represents a sport organized by DouroBats (e.g., Volleyball, Padel).
 * @param id Unique identifier
 * @param name Display name
 * @param description Brief overview of the sport
 * @param iconResource The resource identifier for the UI icon
 */
data class Sport(
    val id: String,
    val name: String,
    val description: String,
    val iconResource: String
)