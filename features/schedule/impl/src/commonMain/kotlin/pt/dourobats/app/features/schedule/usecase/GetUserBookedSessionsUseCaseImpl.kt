package pt.dourobats.app.features.schedule.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.LocalDate
import pt.dourobats.app.core.domain.usecase.GetUserBookedSessionsUseCase
import pt.dourobats.app.features.schedule.api.model.Session
import pt.dourobats.app.features.schedule.repository.TrainingRepository

internal class GetUserBookedSessionsUseCaseImpl(
    private val trainingRepository: TrainingRepository
) : GetUserBookedSessionsUseCase {
    override fun invoke(fromDate: LocalDate): Flow<List<Session>> {
        return combine(
            trainingRepository.getAllSessions(),
            trainingRepository.getUserBookedSessionIds()
        ) { sessions, bookedIds ->
            sessions
                .filter { session ->
                    session.id in bookedIds && session.dateTime.date >= fromDate
                }
                .sortedBy { it.dateTime }
        }
    }
}
