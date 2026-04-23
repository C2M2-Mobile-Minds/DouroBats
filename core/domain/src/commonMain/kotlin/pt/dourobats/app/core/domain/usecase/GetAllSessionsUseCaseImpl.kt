package pt.dourobats.app.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.features.schedule.api.Session
import pt.dourobats.app.features.schedule.api.TrainingRepository

class GetAllSessionsUseCaseImpl(
    private val trainingRepository: TrainingRepository
) : GetAllSessionsUseCase {
    override fun invoke(): Flow<List<Session>> = trainingRepository.getAllSessions()
}
