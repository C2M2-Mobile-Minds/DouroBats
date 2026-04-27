package pt.dourobats.app.features.management.ui

import pt.dourobats.app.features.venues.api.model.Venue

internal data class SessionTemplate(
    val id: String,
    val name: String,
    val type: SessionType,
    val venue: Venue? = null,
    val level: SessionLevel? = null,
    val duration: Int? = null,
    val maxAthletes: String = "",
    val coachName: String = "",
)
