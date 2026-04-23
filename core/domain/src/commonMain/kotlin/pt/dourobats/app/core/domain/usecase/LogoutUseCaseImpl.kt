package pt.dourobats.app.core.domain.usecase

import pt.dourobats.app.features.login.api.repository.AuthRepository

class LogoutUseCaseImpl(
    private val authRepository: AuthRepository
) : LogoutUseCase {
    override suspend fun invoke() {
        authRepository.logout()
    }
}
