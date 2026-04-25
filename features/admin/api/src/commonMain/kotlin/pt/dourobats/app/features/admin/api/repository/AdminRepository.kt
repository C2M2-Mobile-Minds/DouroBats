package pt.dourobats.app.features.admin.api.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.admin.api.model.CalendarUnlock
import pt.dourobats.app.features.schedule.api.model.Session

/**
 * Repository interface for administrative operations.
 */
interface AdminRepository {
    /**
     * Unlocks a calendar period for a specific sport.
     */
    suspend fun unlockCalendar(sportId: String, startDate: LocalDate, endDate: LocalDate): Result<CalendarUnlock>

    /**
     * Gets the history of calendar unlocks for a sport.
     */
    fun getUnlockHistory(sportId: String): Flow<List<CalendarUnlock>>

    /**
     * Updates an athlete's skill level for a specific sport.
     */
    suspend fun updateAthleteLevel(athleteId: String, sportId: String, newLevel: String, reason: String): Result<Unit>

    /**
     * Creates a new training session.
     */
    suspend fun createSession(session: Session): Result<Unit>

    /**
     * Updates an existing training session.
     */
    suspend fun updateSession(session: Session): Result<Unit>
}
