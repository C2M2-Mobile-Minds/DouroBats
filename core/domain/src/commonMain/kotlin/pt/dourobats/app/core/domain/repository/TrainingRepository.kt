package pt.dourobats.app.core.domain.repository

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.core.domain.model.Session


/**
 * Repository interface for training session operations
 * Following Repository Pattern from Clean Architecture
 */
interface TrainingRepository {
    /**
     * Get all training sessions as a Flow
     */
    fun getTrainingSessions(): Flow<List<Session>>

    /**
     * Get a specific training session by ID
     */
    suspend fun getTrainingSessionById(id: String): Session?

    /**
     * Save a new training session
     */
    suspend fun saveTrainingSession(session: Session)
}
