package pt.dourobats.app.features.schedule.data

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import pt.dourobats.app.features.schedule.api.Session
import pt.dourobats.app.features.schedule.api.SessionStatus
import pt.dourobats.app.features.schedule.api.SkillLevel
import pt.dourobats.app.core.ui.model.SessionDisplayData
import kotlin.time.Duration.Companion.hours

/**
 * Mock data for demonstrating session lists.
 * Replace with real repository data when backend is ready.
 */
object MockSessionData {

    /**
     * Generates mock sessions for demonstration.
     * Creates sessions across multiple dates and sports.
     */
    fun generateMockSessions(): List<Session> {
        val baseDate = LocalDate(2026, 1, 12) // Today - update this date as needed

        return listOf(
            // Today's sessions
            createSession(
                id = "session-1",
                sportName = "Volleyball",
                date = baseDate,
                startTime = LocalTime(18, 0),
                durationHours = 2.0,
                venue = "Campo Principal",
                capacity = 20,
                currentAttendees = 15
            ),
            createSession(
                id = "session-2",
                sportName = "Futsal",
                date = baseDate,
                startTime = LocalTime(20, 30),
                durationHours = 1.5,
                venue = "Campo 2",
                capacity = 16,
                currentAttendees = 12
            ),

            // Tomorrow's sessions
            createSession(
                id = "session-3",
                sportName = "Volleyball",
                date = baseDate.plusDays(1),
                startTime = LocalTime(19, 0),
                durationHours = 2.0,
                venue = "Campo Principal",
                capacity = 20,
                currentAttendees = 8
            ),

            // Week ahead sessions
            createSession(
                id = "session-4",
                sportName = "Volleyball",
                date = baseDate.plusDays(3),
                startTime = LocalTime(18, 0),
                durationHours = 2.0,
                venue = "Campo Principal",
                capacity = 20,
                currentAttendees = 10
            ),
            createSession(
                id = "session-5",
                sportName = "Futsal",
                date = baseDate.plusDays(5),
                startTime = LocalTime(17, 0),
                durationHours = 2.0,
                venue = "Campo 2",
                capacity = 16,
                currentAttendees = 14
            )
        )
    }

    /**
     * Returns IDs of sessions the user has booked.
     * In real app, this would come from user's booking data.
     */
    fun getUserBookedSessionIds(): Set<String> {
        return setOf("session-1", "session-4", "session-5")
    }

    private fun createSession(
        id: String,
        sportName: String,
        date: LocalDate,
        startTime: LocalTime,
        durationHours: Double,
        venue: String,
        capacity: Int,
        currentAttendees: Int
    ): Session {
        return Session(
            id = id,
            sportId = sportName.lowercase(),
            dateTime = LocalDateTime(date, startTime),
            duration = (durationHours * 60).toInt().hours,
            venueId = venue,
            targetLevel = SkillLevel.INTERMEDIATE,
            capacity = capacity,
            currentAttendees = currentAttendees,
            status = SessionStatus.SCHEDULED
        )
    }

    private fun LocalDate.plusDays(days: Int): LocalDate {
        return LocalDate.fromEpochDays(this.toEpochDays() + days)
    }
}

/**
 * Converts sessions to display data with feature-specific sport mapping.
 */
fun List<Session>.toDisplayData(bookedIds: Set<String>): List<SessionDisplayData> {
    return map { session ->
        SessionDisplayData(
            session = session,
            sportName = when (session.sportId) {
                "volleyball" -> "Volleyball"
                "futsal" -> "Futsal"
                else -> "Unknown"
            },
            sportIcon = when (session.sportId) {
                "volleyball" -> "🏐"
                "futsal" -> "⚽"
                else -> "🏃"
            },
            venueName = session.venueId,
            isUserBooked = session.id in bookedIds
        )
    }
}
