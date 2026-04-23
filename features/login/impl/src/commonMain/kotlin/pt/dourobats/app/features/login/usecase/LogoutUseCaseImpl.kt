package pt.dourobats.app.features.login.usecase

import pt.dourobats.app.features.login.api.usecase.LogoutUseCase
import pt.dourobats.app.features.login.repository.AuthRepository

internal class LogoutUseCaseImpl(
    private val authRepository: AuthRepository
) : LogoutUseCase {
    override suspend fun invoke() {
        authRepository.logout()
    }
}
