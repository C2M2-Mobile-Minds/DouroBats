package pt.dourobats.app.features.login.api.usecase

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.api.model.LoginMethod

/**
 * Use case for authenticating a user with social login providers.
 */
interface LoginWithSocialUseCase {
    suspend operator fun invoke(method: LoginMethod): Result<Unit>
}
