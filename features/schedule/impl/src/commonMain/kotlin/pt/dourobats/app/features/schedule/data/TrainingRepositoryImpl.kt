package pt.dourobats.app.features.schedule.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.core.common.exception.NetworkException
import pt.dourobats.app.features.schedule.api.model.Session
import pt.dourobats.app.features.schedule.repository.TrainingRepository

/**
 * Production implementation of [TrainingRepository].
 * TODO: Replace with real network/API calls when backend is ready.
 */
internal class TrainingRepositoryImpl : TrainingRepository {

    override fun getSessionsByDate(date: LocalDate): Flow<List<Session>> = flowOf(emptyList())

    override fun getAllSessions(): Flow<List<Session>> = flowOf(emptyList())

    override fun getUserBookedSessionIds(): Flow<Set<String>> = flowOf(emptySet())

    override suspend fun bookSession(sessionId: String): Result<Unit> =
        Result.Error(NetworkException("Not implemented: backend not yet connected"))

    override suspend fun cancelBooking(sessionId: String): Result<Unit> =
        Result.Error(NetworkException("Not implemented: backend not yet connected"))

    override suspend fun getSessionById(id: String): Result<Session> =
        Result.Error(NetworkException("Not implemented: backend not yet connected"))
}
