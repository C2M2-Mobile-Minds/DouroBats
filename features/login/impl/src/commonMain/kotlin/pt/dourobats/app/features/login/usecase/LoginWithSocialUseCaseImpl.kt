package pt.dourobats.app.features.login.usecase

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.api.usecase.LoginWithSocialUseCase
import pt.dourobats.app.features.login.api.model.LoginMethod
import pt.dourobats.app.features.login.repository.AuthRepository

internal class LoginWithSocialUseCaseImpl(
    private val authRepository: AuthRepository
) : LoginWithSocialUseCase {
    override suspend fun invoke(method: LoginMethod): Result<Unit> {
        if (method == LoginMethod.EMAIL) {
            return Result.Error(
                IllegalArgumentException("Use LoginWithEmailUseCase for email authentication")
            )
        }
        return authRepository.loginWithSocial(method)
    }
}
