package pt.dourobats.app.features.schedule.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.schedule.api.model.Session
import pt.dourobats.app.features.schedule.api.model.SessionStatus
import pt.dourobats.app.features.schedule.api.model.SkillLevel
import pt.dourobats.app.features.schedule.repository.TrainingRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours

class GetUserBookedSessionsUseCaseTest {

    @Test
    fun `getUserBookedSessions returns only booked sessions from specified date`() = runTest {
        // Given
        val baseDate = LocalDate(2026, 1, 6)
        val session1 = createTestSession("session-1", baseDate) // Today, booked
        val session2 = createTestSession("session-2", baseDate.plusDays(1)) // Tomorrow, booked
        val session3 = createTestSession("session-3", baseDate.plusDays(2)) // Day after, not booked
        val session4 = createTestSession("session-4", baseDate.minusDays(1)) // Yesterday, booked but before fromDate

        val repository = FakeTrainingRepository(
            allSessions = listOf(session1, session2, session3, session4),
            bookedIds = setOf("session-1", "session-2", "session-4")
        )
        val useCase = GetUserBookedSessionsUseCaseImpl(repository)

        // When
        val result = useCase(fromDate = baseDate).first()

        // Then
        assertEquals(2, result.size)
        assertEquals("session-1", result[0].id)
        assertEquals("session-2", result[1].id)
    }

    @Test
    fun `getUserBookedSessions returns sessions sorted by dateTime`() = runTest {
        // Given
        val baseDate = LocalDate(2026, 1, 6)
        val session1 = createTestSession("session-1", baseDate.plusDays(2), LocalTime(18, 0))
        val session2 = createTestSession("session-2", baseDate, LocalTime(20, 0))
        val session3 = createTestSession("session-3", baseDate.plusDays(1), LocalTime(19, 0))

        val repository = FakeTrainingRepository(
            allSessions = listOf(session1, session2, session3), // Unsorted
            bookedIds = setOf("session-1", "session-2", "session-3")
        )
        val useCase = GetUserBookedSessionsUseCaseImpl(repository)

        // When
        val result = useCase(fromDate = baseDate).first()

        // Then
        assertEquals(3, result.size)
        // Should be sorted by dateTime
        assertEquals("session-2", result[0].id) // Jan 6, 20:00
        assertEquals("session-3", result[1].id) // Jan 7, 19:00
        assertEquals("session-1", result[2].id) // Jan 8, 18:00
    }

    @Test
    fun `getUserBookedSessions returns empty list when no booked sessions`() = runTest {
        // Given
        val baseDate = LocalDate(2026, 1, 6)
        val session1 = createTestSession("session-1", baseDate)
        val session2 = createTestSession("session-2", baseDate.plusDays(1))

        val repository = FakeTrainingRepository(
            allSessions = listOf(session1, session2),
            bookedIds = emptySet() // No bookings
        )
        val useCase = GetUserBookedSessionsUseCaseImpl(repository)

        // When
        val result = useCase(fromDate = baseDate).first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getUserBookedSessions excludes sessions before fromDate`() = runTest {
        // Given
        val baseDate = LocalDate(2026, 1, 10)
        val pastSession = createTestSession("session-past", LocalDate(2026, 1, 5))
        val currentSession = createTestSession("session-current", baseDate)
        val futureSession = createTestSession("session-future", baseDate.plusDays(5))

        val repository = FakeTrainingRepository(
            allSessions = listOf(pastSession, currentSession, futureSession),
            bookedIds = setOf("session-past", "session-current", "session-future")
        )
        val useCase = GetUserBookedSessionsUseCaseImpl(repository)

        // When
        val result = useCase(fromDate = baseDate).first()

        // Then
        assertEquals(2, result.size)
        assertEquals("session-current", result[0].id)
        assertEquals("session-future", result[1].id)
    }

    @Test
    fun `getUserBookedSessions includes sessions on fromDate boundary`() = runTest {
        // Given
        val baseDate = LocalDate(2026, 1, 6)
        val session = createTestSession("session-1", baseDate)

        val repository = FakeTrainingRepository(
            allSessions = listOf(session),
            bookedIds = setOf("session-1")
        )
        val useCase = GetUserBookedSessionsUseCaseImpl(repository)

        // When
        val result = useCase(fromDate = baseDate).first()

        // Then
        assertEquals(1, result.size)
        assertEquals("session-1", result[0].id)
    }

    @Test
    fun `getUserBookedSessions handles multiple sessions on same day sorted by time`() = runTest {
        // Given
        val baseDate = LocalDate(2026, 1, 6)
        val morningSession = createTestSession("session-morning", baseDate, LocalTime(9, 0))
        val afternoonSession = createTestSession("session-afternoon", baseDate, LocalTime(14, 0))
        val eveningSession = createTestSession("session-evening", baseDate, LocalTime(18, 0))

        val repository = FakeTrainingRepository(
            allSessions = listOf(eveningSession, morningSession, afternoonSession), // Unsorted
            bookedIds = setOf("session-morning", "session-afternoon", "session-evening")
        )
        val useCase = GetUserBookedSessionsUseCaseImpl(repository)

        // When
        val result = useCase(fromDate = baseDate).first()

        // Then
        assertEquals(3, result.size)
        assertEquals("session-morning", result[0].id)
        assertEquals("session-afternoon", result[1].id)
        assertEquals("session-evening", result[2].id)
    }

    private fun createTestSession(
        id: String,
        date: LocalDate,
        time: LocalTime = LocalTime(18, 0)
    ): Session {
        return Session(
            id = id,
            sportId = "volleyball",
            dateTime = LocalDateTime(date, time),
            duration = 2.hours,
            venueId = "venue-1",
            targetLevel = SkillLevel.INTERMEDIATE,
            capacity = 20,
            currentAttendees = 10,
            status = SessionStatus.SCHEDULED
        )
    }

    private fun LocalDate.plusDays(days: Int): LocalDate {
        return LocalDate.fromEpochDays(this.toEpochDays() + days)
    }

    private fun LocalDate.minusDays(days: Int): LocalDate {
        return LocalDate.fromEpochDays(this.toEpochDays() - days)
    }

    /**
     * Fake implementation of TrainingRepository for testing.
     */
    private class FakeTrainingRepository(
        private val allSessions: List<Session>,
        private val bookedIds: Set<String>
    ) : TrainingRepository {
        override fun getSessionsByDate(date: LocalDate): Flow<List<Session>> {
            return flowOf(allSessions.filter { it.dateTime.date == date })
        }

        override fun getAllSessions(): Flow<List<Session>> {
            return flowOf(allSessions)
        }

        override fun getUserBookedSessionIds(): Flow<Set<String>> {
            return flowOf(bookedIds)
        }

        override suspend fun bookSession(sessionId: String): Result<Unit> {
            return Result.Success(Unit)
        }

        override suspend fun cancelBooking(sessionId: String): Result<Unit> {
            return Result.Success(Unit)
        }

        override suspend fun getSessionById(id: String): Result<Session> {
            return Result.Error(pt.dourobats.app.core.common.exception.NetworkException("Not implemented"))
        }
    }
}
