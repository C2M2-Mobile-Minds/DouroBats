package pt.dourobats.app.features.schedule.api

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import pt.dourobats.app.core.common.Result

/**
 * Repository interface for training session operations.
 * Following Repository Pattern from Clean Architecture.
 * Designed to work with both mock data and real API.
 */
interface TrainingRepository {
    /**
     * Get all available sessions for a specific date.
     * Returns a Flow for reactive updates when backend supports real-time data.
     */
    fun getSessionsByDate(date: LocalDate): Flow<List<Session>>

    /**
     * Get all sessions (for month/week views).
     * Returns a Flow for reactive updates.
     */
    fun getAllSessions(): Flow<List<Session>>

    /**
     * Get IDs of sessions the current user has booked.
     * Returns a Flow that updates when bookings change.
     */
    fun getUserBookedSessionIds(): Flow<Set<String>>

    /**
     * Book a session for the current user.
     * API-ready: returns Result for proper error handling.
     */
    suspend fun bookSession(sessionId: String): Result<Unit>

    /**
     * Cancel a booking for the current user.
     * API-ready: returns Result for proper error handling.
     */
    suspend fun cancelBooking(sessionId: String): Result<Unit>

    /**
     * Get a specific session by ID.
     * Useful for detail views.
     */
    suspend fun getSessionById(id: String): Result<Session>
}
