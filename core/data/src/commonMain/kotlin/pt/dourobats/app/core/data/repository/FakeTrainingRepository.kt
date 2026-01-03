package pt.dourobats.app.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDateTime
import pt.dourobats.app.core.domain.model.Session
import pt.dourobats.app.core.domain.model.SessionStatus
import pt.dourobats.app.core.domain.model.SkillLevel
import pt.dourobats.app.core.domain.repository.TrainingRepository
import kotlin.time.Duration.Companion.hours

/**
 * Fake implementation of TrainingRepository for MVP
 * Will be replaced with real implementation later
 */
class FakeTrainingRepository : TrainingRepository {

    private val fakeSessions = listOf(
        Session(
            id = "1",
            sportId = "12345",
            dateTime = LocalDateTime(2025, 12, 30, 19, 0),
            duration = 1.hours,
            venueId = "54321",
            targetLevel = SkillLevel.ADVANCED,
            capacity = 50,
            currentAttendees = 18,
            status = SessionStatus.SCHEDULED
        ),
        Session(
            id = "2",
            sportId = "12345",
            dateTime = LocalDateTime(2025, 12, 30, 19, 0),
            duration = 2.hours,
            venueId = "54321",
            targetLevel = SkillLevel.INTERMEDIATE,
            capacity = 50,
            currentAttendees = 18,
            status = SessionStatus.SCHEDULED,
        )
    )

    override fun getTrainingSessions(): Flow<List<Session>> {
        return flowOf(fakeSessions)
    }

    override suspend fun getTrainingSessionById(id: String): Session? {
        return fakeSessions.find { it.id == id }
    }

    override suspend fun saveTrainingSession(session: Session) {
        // No-op for fake implementation
    }
}
