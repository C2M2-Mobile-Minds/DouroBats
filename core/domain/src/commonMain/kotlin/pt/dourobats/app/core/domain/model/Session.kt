package pt.dourobats.app.core.domain.model

import kotlinx.datetime.LocalDateTime

/**
 * Domain model representing a training session
 *
 * I wasn't sure if I should detele this

data class TrainingSession(
val id: String,
val title: String,
val description: String,
val startTime: LocalDateTime,
val endTime: LocalDateTime,
val location: String,
val attendees: Int = 0
)
 */

/**
 * Domain model representing a Session
 */
data class Session (
    val id: String,
    val sportId: String,
    val dateTime: LocalDateTime,
    val duration: Int,
    val venueId: String,
    val targetLevel: SkillLevel,
    val capacity: Int,
    val currentAttendees: Int,
    val status: SessionStatus
) {
}
