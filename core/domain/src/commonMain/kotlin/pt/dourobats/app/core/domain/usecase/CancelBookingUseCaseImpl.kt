package pt.dourobats.app.core.domain.usecase

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.core.common.exception.ValidationException
import pt.dourobats.app.core.repository.TrainingRepository

class CancelBookingUseCaseImpl(
    private val trainingRepository: TrainingRepository
) : CancelBookingUseCase {
    override suspend fun invoke(sessionId: String): Result<Unit> {
        if (sessionId.isBlank()) {
            return Result.Error(
                ValidationException.RequiredField("Session ID")
            )
        }
        return trainingRepository.cancelBooking(sessionId)
    }
}
