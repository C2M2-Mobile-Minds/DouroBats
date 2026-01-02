package pt.dourobats.app.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDateTime
import pt.dourobats.app.core.domain.model.Session
import pt.dourobats.app.core.domain.model.SessionStatus
import pt.dourobats.app.core.domain.model.SkillLevel
import pt.dourobats.app.core.domain.repository.TrainingRepository
import kotlin.time.Duration.Companion.hours

class FakeTrainingRepository : TrainingRepository {

    private val fakeSessions = listOf(
        Session(
            id = "1",
            sportId = "volleyball",
            dateTime = LocalDateTime(2025, 12, 30, 19, 0),
            duration = 2.hours,
            venueId = "pavilhao_1",
            targetLevel = SkillLevel.INTERMEDIATE,
            capacity = 20,
            currentAttendees = 12,
            status = SessionStatus.SCHEDULED
        ),
        Session(
            id = "2",
            sportId = "padel",
            dateTime = LocalDateTime(2026, 1, 2, 19, 0),
            duration = 1.hours,
            venueId = "court_3",
            targetLevel = SkillLevel.BEGINNER,
            capacity = 4,
            currentAttendees = 4,
            status = SessionStatus.SCHEDULED
        )
    )

    override fun getTrainingSessions(): Flow<List<Session>> {
        return flowOf(fakeSessions)
    }

    override suspend fun getTrainingSessionById(id: String): Session? {
        return fakeSessions.find { it.id == id }
    }

    override suspend fun saveTrainingSession(session: Session) {
        // No-op
    }
}