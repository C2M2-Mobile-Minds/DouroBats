package pt.dourobats.app.core.domain.model

import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration

/**
 * The primary entity for the scheduling system.
 * Represents a specific training event at a specific time and place.
 */
data class Session(
    val id: String,
    val sportId: String,
    val dateTime: LocalDateTime,
    val duration: Duration,
    val venueId: String,
    val targetLevel: SkillLevel,
    val capacity: Int,
    val currentAttendees: Int,
    val status: SessionStatus
) {
    /**
     * Business logic helper to determine if the session can accept more athletes.
     */
    val isFull: Boolean get() = currentAttendees >= capacity
}

class Foo(){
    fun test(){}
}