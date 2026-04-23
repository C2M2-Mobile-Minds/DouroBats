package pt.dourobats.app.core.domain.usecase

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.core.common.exception.ValidationException
import pt.dourobats.app.features.schedule.api.TrainingRepository

/**
 * Use case for canceling a session booking.
 * Validates input and delegates to repository.
 */
interface CancelBookingUseCase {
    /**
     * Cancel a booking for the current user.
     * Validates session ID before calling repository.
     *
     * @param sessionId The ID of the session to cancel
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(sessionId: String): Result<Unit>
}

