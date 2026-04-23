package pt.dourobats.app.core.domain.usecase

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.core.common.exception.ValidationException
import pt.dourobats.app.features.schedule.api.repository.TrainingRepository

class BookSessionUseCaseImpl(
    private val trainingRepository: TrainingRepository
) : BookSessionUseCase {
    override suspend fun invoke(sessionId: String): Result<Unit> {
        if (sessionId.isBlank()) {
            return Result.Error(
                ValidationException.RequiredField("Session ID")
            )
        }
        return trainingRepository.bookSession(sessionId)
    }
}
