package pt.dourobats.app.features.schedule.usecase

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.core.common.exception.ValidationException
import pt.dourobats.app.features.schedule.api.usecase.CancelBookingUseCase
import pt.dourobats.app.features.schedule.repository.TrainingRepository

internal class CancelBookingUseCaseImpl(
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
