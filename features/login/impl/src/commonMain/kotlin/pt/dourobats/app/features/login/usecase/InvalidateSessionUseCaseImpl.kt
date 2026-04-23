package pt.dourobats.app.features.login.usecase

import pt.dourobats.app.features.login.api.usecase.InvalidateSessionUseCase
import pt.dourobats.app.features.login.repository.AuthRepository

internal class InvalidateSessionUseCaseImpl(
    private val authRepository: AuthRepository
) : InvalidateSessionUseCase {
    override suspend fun invoke() = authRepository.logout()
}
