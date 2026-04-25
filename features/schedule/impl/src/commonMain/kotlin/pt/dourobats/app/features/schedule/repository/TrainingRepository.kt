package pt.dourobats.app.features.schedule.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.schedule.api.model.Session

internal interface TrainingRepository {
    fun getSessionsByDate(date: LocalDate): Flow<List<Session>>
    fun getAllSessions(): Flow<List<Session>>
    fun getUserBookedSessionIds(): Flow<Set<String>>
    suspend fun bookSession(sessionId: String): Result<Unit>
    suspend fun cancelBooking(sessionId: String): Result<Unit>
    suspend fun getSessionById(id: String): Result<Session>
}
