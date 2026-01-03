package pt.dourobats.app.core.domain.repository

import kotlinx.coroutines.flow.Flow


/**
 * Repository interface for training session operations
 * Following Repository Pattern from Clean Architecture

interface TrainingRepository {
    /**
     * Get all training sessions as a Flow
     */
    fun getTrainingSessions(): Flow<List<TrainingSession>>

    /**
     * Get a specific training session by ID
     */
    suspend fun getTrainingSessionById(id: String): TrainingSession?

    /**
     * Save a new training session
     */
    suspend fun saveTrainingSession(session: TrainingSession)
}
 */