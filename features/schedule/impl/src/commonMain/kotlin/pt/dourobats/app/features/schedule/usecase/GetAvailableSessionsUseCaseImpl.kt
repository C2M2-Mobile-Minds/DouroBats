package pt.dourobats.app.features.schedule.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.LocalDate
import pt.dourobats.app.features.schedule.api.usecase.GetAvailableSessionsUseCase
import pt.dourobats.app.features.schedule.api.model.Session
import pt.dourobats.app.features.schedule.repository.TrainingRepository

internal class GetAvailableSessionsUseCaseImpl(
    private val trainingRepository: TrainingRepository
) : GetAvailableSessionsUseCase {
    override fun invoke(date: LocalDate): Flow<List<Pair<Session, Boolean>>> {
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
