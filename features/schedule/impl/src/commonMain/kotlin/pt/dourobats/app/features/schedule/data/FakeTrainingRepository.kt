package pt.dourobats.app.features.schedule.data

import kotlin.time.Clock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.core.common.exception.NetworkException
import pt.dourobats.app.features.schedule.api.model.Session
import pt.dourobats.app.features.schedule.api.model.SessionStatus
import pt.dourobats.app.features.schedule.api.model.SkillLevel
import pt.dourobats.app.features.schedule.repository.TrainingRepository
import kotlin.time.Duration.Companion.hours

/**
 * Fake implementation of TrainingRepository for MVP.
 * Uses in-memory data that simulates API behavior.
 * Will be replaced with real API implementation later.
 *
 * When switching to real API:
 * 1. Replace MutableStateFlow with API calls
 * 2. Map API responses to domain models
 * 3. Handle network errors properly
 * 4. Add caching if needed
 */
internal class FakeTrainingRepository : TrainingRepository {

    // Simulates API data - replace with real API calls
    private val _sessions = MutableStateFlow(generateMockSessions())
    private val _bookedSessionIds = MutableStateFlow(
        setOf("session-1", "session-4", "session-5")
    )

    override fun getSessionsByDate(date: LocalDate): Flow<List<Session>> {
        return _sessions.map { sessions ->
            sessions.filter { it.dateTime.date == date }
        }
    }

    override fun getAllSessions(): Flow<List<Session>> {
        return _sessions
    }

    override fun getUserBookedSessionIds(): Flow<Set<String>> {
        return _bookedSessionIds
    }

    override suspend fun bookSession(sessionId: String): Result<Unit> {
        // Simulate API call
        val session = _sessions.value.find { it.id == sessionId }
            ?: return Result.Error(NetworkException("Session not found"))

        if (session.currentAttendees >= session.capacity) {
            return Result.Error(NetworkException("Session is full"))
        }

        // Update local state (simulates API success)
        _bookedSessionIds.value = _bookedSessionIds.value + sessionId
        return Result.Success(Unit)
    }

    override suspend fun cancelBooking(sessionId: String): Result<Unit> {
        // Simulate API call
        if (sessionId !in _bookedSessionIds.value) {
            return Result.Error(NetworkException("Booking not found"))
        }

        // Update local state (simulates API success)
        _bookedSessionIds.value = _bookedSessionIds.value - sessionId
        return Result.Success(Unit)
    }

    override suspend fun getSessionById(id: String): Result<Session> {
        // Simulate API call
        val session = _sessions.value.find { it.id == id }
            ?: return Result.Error(NetworkException("Session not found"))

        return Result.Success(session)
    }

    /**
     * Generates mock sessions for demonstration.
     * In real implementation, this data would come from API.
     */
    private fun generateMockSessions(): List<Session> {
        val baseDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        return listOf(
            // Today's sessions
            createSession(
                id = "session-1",
                sportId = "volleyball",
                date = baseDate,
                startTime = LocalTime(18, 0),
                durationHours = 2.0,
                venueId = "venue-1",
                capacity = 20,
                currentAttendees = 15
            ),
            createSession(
                id = "session-2",
                sportId = "futsal",
                date = baseDate,
                startTime = LocalTime(20, 30),
                durationHours = 1.5,
                venueId = "venue-2",
                capacity = 16,
                currentAttendees = 12
            ),

            // Tomorrow's sessions
            createSession(
                id = "session-3",
                sportId = "volleyball",
                date = baseDate.plusDays(1),
                startTime = LocalTime(19, 0),
                durationHours = 2.0,
                venueId = "venue-1",
                capacity = 20,
                currentAttendees = 8
            ),

            // Week ahead sessions
            createSession(
                id = "session-4",
                sportId = "volleyball",
                date = baseDate.plusDays(3),
                startTime = LocalTime(18, 0),
                durationHours = 2.0,
                venueId = "venue-1",
                capacity = 20,
                currentAttendees = 10
            ),
            createSession(
                id = "session-5",
                sportId = "futsal",
                date = baseDate.plusDays(5),
                startTime = LocalTime(17, 0),
                durationHours = 2.0,
                venueId = "venue-2",
                capacity = 16,
                currentAttendees = 14
            )
        )
    }

    private fun createSession(
        id: String,
        sportId: String,
        date: LocalDate,
        startTime: LocalTime,
        durationHours: Double,
        venueId: String,
        capacity: Int,
        currentAttendees: Int
    ): Session {
        return Session(
            id = id,
            sportId = sportId,
            dateTime = LocalDateTime(date, startTime),
            duration = (durationHours * 60).toInt().hours,
            venueId = venueId,
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
