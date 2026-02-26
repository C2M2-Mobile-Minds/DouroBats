package pt.dourobats.app.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.core.model.Session
import pt.dourobats.app.core.repository.TrainingRepository

class GetAllSessionsUseCaseImpl(
    private val trainingRepository: TrainingRepository
) : GetAllSessionsUseCase {
    override fun invoke(): Flow<List<Session>> = trainingRepository.getAllSessions()
}
