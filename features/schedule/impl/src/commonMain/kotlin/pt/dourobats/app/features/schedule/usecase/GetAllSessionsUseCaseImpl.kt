package pt.dourobats.app.features.schedule.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.core.domain.usecase.GetAllSessionsUseCase
import pt.dourobats.app.features.schedule.api.model.Session
import pt.dourobats.app.features.schedule.repository.TrainingRepository

internal class GetAllSessionsUseCaseImpl(
    private val trainingRepository: TrainingRepository
) : GetAllSessionsUseCase {
    override fun invoke(): Flow<List<Session>> = trainingRepository.getAllSessions()
}
