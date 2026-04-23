package pt.dourobats.app.core.domain.usecase

/**
 * Use case for logging out the current user.
 */
interface LogoutUseCase {
    suspend operator fun invoke()
}

