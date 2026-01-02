package pt.dourobats.app.core.domain.model

import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours

class SessionTest {

    @Test
    fun isFull_shouldReturnTrue_whenAttendeesAtCapacity() {
        val session = createTestSession(capacity = 10, attendees = 10)
        assertTrue(session.isFull)
    }

    @Test
    fun isFull_shouldReturnFalse_whenAttendeesBelowCapacity() {
        val session = createTestSession(capacity = 10, attendees = 5)
        assertFalse(session.isFull)
    }

    private fun createTestSession(capacity: Int, attendees: Int) = Session(
        id = "test-1",
        sportId = "volleyball",
        dateTime = LocalDateTime(2026, 1, 1, 12, 0),
        duration = 2.hours,
        venueId = "v1",
        targetLevel = SkillLevel.BEGINNER,
        capacity = capacity,
        currentAttendees = attendees,
        status = SessionStatus.SCHEDULED
    )
}