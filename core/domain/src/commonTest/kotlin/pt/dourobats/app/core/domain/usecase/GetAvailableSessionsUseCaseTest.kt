package pt.dourobats.app.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.core.model.Session
import pt.dourobats.app.core.model.SessionStatus
import pt.dourobats.app.core.model.SkillLevel
import pt.dourobats.app.core.repository.TrainingRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours

class GetAvailableSessionsUseCaseTest {

    @Test
    fun `getAvailableSessions returns sessions with booking status`() = runTest {
        // Given
        val date = LocalDate(2026, 1, 6)
        val session1 = createTestSession("session-1", date)
        val session2 = createTestSession("session-2", date)
        val repository = FakeTrainingRepository(
            sessionsForDate = listOf(session1, session2),
            bookedIds = setOf("session-1")
        )
        val useCase = GetAvailableSessionsUseCaseImpl(repository)

        // When
        val result = useCase(date).first()

        // Then
        assertEquals(2, result.size)

        val (firstSession, firstBooked) = result.first { it.first.id == "session-1" }
        assertTrue(firstBooked)
        assertEquals("session-1", firstSession.id)

        val (secondSession, secondBooked) = result.first { it.first.id == "session-2" }
        assertFalse(secondBooked)
        assertEquals("session-2", secondSession.id)
    }

    @Test
    fun `getAvailableSessions returns empty list when no sessions for date`() = runTest {
        // Given
        val date = LocalDate(2026, 1, 6)
        val repository = FakeTrainingRepository(
            sessionsForDate = emptyList(),
            bookedIds = emptySet()
        )
        val useCase = GetAvailableSessionsUseCaseImpl(repository)

        // When
        val result = useCase(date).first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getAvailableSessions marks all sessions as not booked when user has no bookings`() = runTest {
        // Given
        val date = LocalDate(2026, 1, 6)
        val session1 = createTestSession("session-1", date)
        val session2 = createTestSession("session-2", date)
        val repository = FakeTrainingRepository(
            sessionsForDate = listOf(session1, session2),
            bookedIds = emptySet()
        )
        val useCase = GetAvailableSessionsUseCaseImpl(repository)

        // When
        val result = useCase(date).first()

        // Then
        assertEquals(2, result.size)
        result.forEach { (_, isBooked) ->
            assertFalse(isBooked)
        }
    }

    @Test
    fun `getAvailableSessions marks all sessions as booked when user booked all`() = runTest {
        // Given
        val date = LocalDate(2026, 1, 6)
        val session1 = createTestSession("session-1", date)
        val session2 = createTestSession("session-2", date)
        val repository = FakeTrainingRepository(
            sessionsForDate = listOf(session1, session2),
            bookedIds = setOf("session-1", "session-2")
        )
        val useCase = GetAvailableSessionsUseCaseImpl(repository)

        // When
        val result = useCase(date).first()

        // Then
        assertEquals(2, result.size)
        result.forEach { (_, isBooked) ->
            assertTrue(isBooked)
        }
    }

    @Test
    fun `getAvailableSessions emits new values when bookings change`() = runTest {
        // Given
        val date = LocalDate(2026, 1, 6)
        val session = createTestSession("session-1", date)
        val repository = FakeTrainingRepository(
            sessionsForDate = listOf(session),
            bookedIds = emptySet()
        )
        val useCase = GetAvailableSessionsUseCaseImpl(repository)

        // When - first emission
        val result1 = useCase(date).first()

        // Then
        assertFalse(result1.first().second)

        // When - update bookings
        repository.updateBookedIds(setOf("session-1"))
        val result2 = useCase(date).first()

        // Then
        assertTrue(result2.first().second)
    }

    private fun createTestSession(id: String, date: LocalDate): Session {
        return Session(
            id = id,
            sportId = "volleyball",
            dateTime = LocalDateTime(date, LocalTime(18, 0)),
            duration = 2.hours,
            venueId = "venue-1",
            targetLevel = SkillLevel.INTERMEDIATE,
            capacity = 20,
            currentAttendees = 10,
            status = SessionStatus.SCHEDULED
        )
    }

    /**
     * Fake implementation of TrainingRepository for testing.
     */
    private class FakeTrainingRepository(
        private val sessionsForDate: List<Session>,
        bookedIds: Set<String>
    ) : TrainingRepository {
        private val _bookedIds = MutableStateFlow(bookedIds)

        fun updateBookedIds(ids: Set<String>) {
            _bookedIds.value = ids
        }

        override fun getSessionsByDate(date: LocalDate): Flow<List<Session>> {
            return flowOf(sessionsForDate)
        }

        override fun getAllSessions(): Flow<List<Session>> {
            return flowOf(sessionsForDate)
        }

        override fun getUserBookedSessionIds(): Flow<Set<String>> {
            return _bookedIds
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
