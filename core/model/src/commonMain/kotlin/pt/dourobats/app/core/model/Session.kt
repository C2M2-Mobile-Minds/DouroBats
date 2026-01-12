package pt.dourobats.app.core.model

import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration

/**
 * Domain model representing a session
 *
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
)

