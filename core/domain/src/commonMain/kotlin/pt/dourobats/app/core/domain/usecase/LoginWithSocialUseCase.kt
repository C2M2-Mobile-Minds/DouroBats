package pt.dourobats.app.core.domain.usecase

import pt.dourobats.app.core.domain.common.Result
import pt.dourobats.app.core.domain.model.LoginMethod
import pt.dourobats.app.core.domain.repository.AuthRepository

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
open class LoginWithSocialUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Authenticates a user with a social login provider.
     *
     * @param method The social login method to use (Google, Facebook, or Apple)
     * @return Result indicating success or failure
     * @throws IllegalArgumentException if EMAIL method is provided
     */
    open suspend operator fun invoke(method: LoginMethod): Result<Unit> {
        // Prevent misuse - EMAIL method should use LoginWithEmailUseCase
        if (method == LoginMethod.EMAIL) {
            return Result.Error(
                IllegalArgumentException("Use LoginWithEmailUseCase for email authentication")
            )
        }

        // Delegate to repository for social authentication
        return authRepository.loginWithSocial(method)
    }
}
