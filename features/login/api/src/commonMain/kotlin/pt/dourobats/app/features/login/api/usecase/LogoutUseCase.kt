package pt.dourobats.app.features.login.api.usecase

/**
 * Use case for logging out the current user.
 */
interface LogoutUseCase {
    suspend operator fun invoke()
}
