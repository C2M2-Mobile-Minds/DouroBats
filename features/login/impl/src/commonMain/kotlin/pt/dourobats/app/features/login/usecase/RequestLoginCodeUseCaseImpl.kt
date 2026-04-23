package pt.dourobats.app.features.login.usecase

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.api.usecase.RequestLoginCodeUseCase
import pt.dourobats.app.features.login.repository.AuthRepository

internal class RequestLoginCodeUseCaseImpl(
    private val authRepository: AuthRepository
) : RequestLoginCodeUseCase {
    override suspend fun invoke(email: String): Result<Unit> =
        authRepository.requestLoginCode(email)
}
