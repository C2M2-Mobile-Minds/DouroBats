package pt.dourobats.app.features.schedule.api.model

/**
 * Represents a sport that can be associated with training sessions.
 */
data class Sport(
    val id: String,
    val name: String,
    val description: String,
    val iconResource: String
)
