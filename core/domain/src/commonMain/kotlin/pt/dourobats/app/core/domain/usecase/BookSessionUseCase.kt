package pt.dourobats.app.core.domain.usecase

import pt.dourobats.app.core.domain.common.Result
import pt.dourobats.app.core.domain.exception.ValidationException
import pt.dourobats.app.core.domain.repository.TrainingRepository

/**
 * Use case for booking a training session.
 * Validates input and delegates to repository.
 */
open class BookSessionUseCase(
    private val trainingRepository: TrainingRepository
) {
    /**
     * Book a session for the current user.
     * Validates session ID before calling repository.
     *
     * @param sessionId The ID of the session to book
     * @return Result indicating success or failure
     */
    open suspend operator fun invoke(sessionId: String): Result<Unit> {
        if (sessionId.isBlank()) {
            return Result.Error(
                ValidationException.RequiredField("Session ID")
            )
        }

        return trainingRepository.bookSession(sessionId)
    }
}
