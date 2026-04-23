package pt.dourobats.app.core.domain.usecase

import pt.dourobats.app.core.common.Result

/**
 * Use case for booking a training session.
 * Validates input and delegates to repository.
 */
interface BookSessionUseCase {
    /**
     * Book a session for the current user.
     * Validates session ID before calling repository.
     *
     * @param sessionId The ID of the session to book
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(sessionId: String): Result<Unit>
}