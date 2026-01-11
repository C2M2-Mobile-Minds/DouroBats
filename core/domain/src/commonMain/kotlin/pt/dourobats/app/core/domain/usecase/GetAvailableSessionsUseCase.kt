package pt.dourobats.app.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.LocalDate
import pt.dourobats.app.core.domain.model.Session
import pt.dourobats.app.core.domain.repository.TrainingRepository

/**
 * Use case for retrieving available training sessions for a specific date.
 * Combines session data with user booking status.
 */
open class GetAvailableSessionsUseCase(
    private val trainingRepository: TrainingRepository
) {
    /**
     * Get all sessions for a specific date with booking status.
     * Returns a Flow that emits whenever sessions or bookings change.
     *
     * @param date The date to get sessions for
     * @return Flow of pairs (Session, isBooked)
     */
    open operator fun invoke(date: LocalDate): Flow<List<Pair<Session, Boolean>>> {
        return combine(
            trainingRepository.getSessionsByDate(date),
            trainingRepository.getUserBookedSessionIds()
        ) { sessions, bookedIds ->
            sessions.map { session ->
                session to (session.id in bookedIds)
            }
        }
    }
}
