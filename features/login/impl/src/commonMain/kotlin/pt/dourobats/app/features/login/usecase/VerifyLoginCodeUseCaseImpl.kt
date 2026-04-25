package pt.dourobats.app.features.login.usecase

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.api.usecase.VerifyLoginCodeUseCase
import pt.dourobats.app.features.login.repository.AuthRepository

internal class VerifyLoginCodeUseCaseImpl(
    private val authRepository: AuthRepository
) : VerifyLoginCodeUseCase {
    override suspend fun invoke(email: String, code: String): Result<Unit> =
        authRepository.verifyLoginCode(email, code)
}
