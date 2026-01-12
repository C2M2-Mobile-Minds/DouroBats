package pt.dourobats.app.core.domain.usecase

import pt.dourobats.app.core.repository.AuthRepository

class LogoutUseCaseImpl(
    private val authRepository: AuthRepository
) : LogoutUseCase {
    override suspend fun invoke() {
        authRepository.logout()
    }
}
