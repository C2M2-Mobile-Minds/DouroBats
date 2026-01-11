package pt.dourobats.app.features.schedule.data

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import pt.dourobats.app.core.domain.model.SessionStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MockSessionDataTest {

    @Test
    fun `generateMockSessions returns non-empty list`() {
        // Act
        val sessions = MockSessionData.generateMockSessions()

        // Assert
        assertTrue(sessions.isNotEmpty(), "Mock sessions should not be empty")
    }

    @Test
    fun `generateMockSessions creates sessions with valid data`() {
        // Act
        val sessions = MockSessionData.generateMockSessions()

        // Assert
        sessions.forEach { session ->
            assertTrue(session.id.isNotBlank(), "Session ID should not be blank")
            assertTrue(session.sportId.isNotBlank(), "Sport ID should not be blank")
            assertTrue(session.capacity > 0, "Capacity should be positive")
            assertTrue(session.currentAttendees >= 0, "Current attendees should not be negative")
            assertTrue(session.currentAttendees <= session.capacity,
                "Current attendees should not exceed capacity")
            assertEquals(SessionStatus.SCHEDULED, session.status, "All mock sessions should be scheduled")
        }
    }

    @Test
    fun `generateMockSessions includes today's sessions`() {
        // Arrange
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

        // Act
        val sessions = MockSessionData.generateMockSessions()

        // Assert
        val todaySessions = sessions.filter { it.dateTime.date == today }
        assertTrue(todaySessions.isNotEmpty(), "Should have sessions for today")
    }

    @Test
    fun `generateMockSessions includes future sessions`() {
        // Arrange
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

        // Act
        val sessions = MockSessionData.generateMockSessions()

        // Assert
        val futureSessions = sessions.filter { it.dateTime.date > today }
        assertTrue(futureSessions.isNotEmpty(), "Should have future sessions")
    }

    @Test
    fun `generateMockSessions includes multiple sports`() {
        // Act
        val sessions = MockSessionData.generateMockSessions()

        // Assert
        val sportIds = sessions.map { it.sportId }.distinct()
        assertTrue(sportIds.size > 1, "Should have sessions for multiple sports")
    }

    @Test
    fun `getUserBookedSessionIds returns valid session IDs`() {
        // Act
        val bookedIds = MockSessionData.getUserBookedSessionIds()
        val allSessions = MockSessionData.generateMockSessions()

        // Assert
        assertTrue(bookedIds.isNotEmpty(), "Should have some booked sessions")
        bookedIds.forEach { bookedId ->
            assertTrue(
                allSessions.any { it.id == bookedId },
                "Booked ID $bookedId should exist in mock sessions"
            )
        }
    }

    @Test
    fun `toDisplayData correctly maps session data`() {
        // Arrange
        val sessions = MockSessionData.generateMockSessions()
        val bookedIds = MockSessionData.getUserBookedSessionIds()

        // Act
        val displayData = sessions.toDisplayData(bookedIds)

        // Assert
        assertEquals(sessions.size, displayData.size, "Display data should match sessions count")

        displayData.forEach { data ->
            assertTrue(data.sportName.isNotBlank(), "Sport name should not be blank")
            assertTrue(data.sportIcon.isNotBlank(), "Sport icon should not be blank")
            assertTrue(data.venueName.isNotBlank(), "Venue name should not be blank")
        }
    }

    @Test
    fun `toDisplayData correctly identifies booked sessions`() {
        // Arrange
        val sessions = MockSessionData.generateMockSessions()
        val bookedIds = MockSessionData.getUserBookedSessionIds()

        // Act
        val displayData = sessions.toDisplayData(bookedIds)

        // Assert
        val bookedCount = displayData.count { it.isUserBooked }
        assertEquals(bookedIds.size, bookedCount, "Booked count should match booked IDs count")

        displayData.forEach { data ->
            if (data.session.id in bookedIds) {
                assertTrue(data.isUserBooked, "Session ${data.session.id} should be marked as booked")
            } else {
                assertFalse(data.isUserBooked, "Session ${data.session.id} should not be marked as booked")
            }
        }
    }

    @Test
    fun `toDisplayData maps volleyball to correct icon and name`() {
        // Arrange
        val sessions = MockSessionData.generateMockSessions()
        val bookedIds = MockSessionData.getUserBookedSessionIds()

        // Act
        val displayData = sessions.toDisplayData(bookedIds)

        // Assert
        val volleyballSessions = displayData.filter { it.session.sportId == "volleyball" }
        volleyballSessions.forEach { data ->
            assertEquals("Volleyball", data.sportName, "Volleyball should have correct name")
            assertEquals("🏐", data.sportIcon, "Volleyball should have correct icon")
        }
    }

    @Test
    fun `toDisplayData maps futsal to correct icon and name`() {
        // Arrange
        val sessions = MockSessionData.generateMockSessions()
        val bookedIds = MockSessionData.getUserBookedSessionIds()

        // Act
        val displayData = sessions.toDisplayData(bookedIds)

        // Assert
        val futsalSessions = displayData.filter { it.session.sportId == "futsal" }
        futsalSessions.forEach { data ->
            assertEquals("Futsal", data.sportName, "Futsal should have correct name")
            assertEquals("⚽", data.sportIcon, "Futsal should have correct icon")
        }
    }

    @Test
    fun `toDisplayData uses venue ID as venue name`() {
        // Arrange
        val sessions = MockSessionData.generateMockSessions()
        val bookedIds = MockSessionData.getUserBookedSessionIds()

        // Act
        val displayData = sessions.toDisplayData(bookedIds)

        // Assert
        displayData.forEach { data ->
            assertEquals(data.session.venueId, data.venueName,
                "Venue name should match venue ID in mock data")
        }
    }
}
