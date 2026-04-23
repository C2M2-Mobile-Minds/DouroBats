package pt.dourobats.app.core.domain.usecase

import pt.dourobats.app.features.login.api.AuthRepository

class LogoutUseCaseImpl(
    private val authRepository: AuthRepository
) : LogoutUseCase {
    override suspend fun invoke() {
        authRepository.logout()
    }
}
