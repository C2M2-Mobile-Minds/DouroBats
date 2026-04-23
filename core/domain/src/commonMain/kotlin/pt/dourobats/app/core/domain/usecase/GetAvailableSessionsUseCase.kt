package pt.dourobats.app.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.LocalDate
import pt.dourobats.app.features.schedule.api.Session
import pt.dourobats.app.features.schedule.api.TrainingRepository

/**
 * Use case for retrieving available training sessions for a specific date.
 * Combines session data with user booking status.
 */
interface GetAvailableSessionsUseCase {
    /**
     * Get all sessions for a specific date with booking status.
     * Returns a Flow that emits whenever sessions or bookings change.
     *
     * @param date The date to get sessions for
     * @return Flow of pairs (Session, isBooked)
     */
    operator fun invoke(date: LocalDate): Flow<List<Pair<Session, Boolean>>>
}

