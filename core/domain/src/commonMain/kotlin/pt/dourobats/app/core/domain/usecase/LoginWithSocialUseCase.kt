package pt.dourobats.app.core.domain.usecase

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.api.model.LoginMethod
import pt.dourobats.app.features.login.api.repository.AuthRepository

/**
 * Use case for authenticating a user with social login providers.
 *
 * Supports authentication via:
 * - Google
 * - Facebook
 * - Apple ID
 *
 * This use case ensures that social authentication is only used with
 * appropriate providers and not with email/password method.
 *
 * @property authRepository Repository for authentication operations
 */
interface LoginWithSocialUseCase {
    /**
     * Authenticates a user with a social login provider.
     *
     * @param method The social login method to use (Google, Facebook, or Apple)
     * @return Result indicating success or failure
     * @throws IllegalArgumentException if EMAIL method is provided
     */
    suspend operator fun invoke(method: LoginMethod): Result<Unit>
}

