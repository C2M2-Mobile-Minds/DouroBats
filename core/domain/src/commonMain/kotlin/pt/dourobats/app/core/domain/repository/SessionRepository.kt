package pt.dourobats.app.core.domain.repository

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.core.domain.model.Session

/**
 * Repository interface session operations
 * Following Repository Pattern from Clean Architecture
 */
interface SessionRepository {
    /**
     * Returns all sessions as a reactive stream.
     */
    fun getSessions(): Flow<List<Session>>

    /**
     * Returns a session by its unique identifier.
     * Returns null if the session does not exist.
     */
    suspend fun getSessionById(id: String): Session?

    /**
     * Persists a session.
     */
    suspend fun saveSession(session: Session): Session
}