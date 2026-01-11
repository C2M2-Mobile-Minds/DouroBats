package pt.dourobats.app.core.domain.usecase

import pt.dourobats.app.core.domain.common.Result
import pt.dourobats.app.core.domain.exception.ValidationException
import pt.dourobats.app.core.domain.repository.TrainingRepository

/**
 * Use case for canceling a session booking.
 * Validates input and delegates to repository.
 */
open class CancelBookingUseCase(
    private val trainingRepository: TrainingRepository
) {
    /**
     * Cancel a booking for the current user.
     * Validates session ID before calling repository.
     *
     * @param sessionId The ID of the session to cancel
     * @return Result indicating success or failure
     */
    open suspend operator fun invoke(sessionId: String): Result<Unit> {
        if (sessionId.isBlank()) {
            return Result.Error(
                ValidationException.RequiredField("Session ID")
            )
        }

        return trainingRepository.cancelBooking(sessionId)
    }
}
