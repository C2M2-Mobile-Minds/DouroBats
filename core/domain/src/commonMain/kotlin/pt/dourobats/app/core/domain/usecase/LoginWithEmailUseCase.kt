package pt.dourobats.app.core.domain.usecase

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.core.common.exception.ValidationException
import pt.dourobats.app.core.repository.AuthRepository

/**
 * Use case for authenticating a user with email and password.
 *
 * This use case encapsulates the business logic for email/password authentication,
 * including validation and error handling. It follows Clean Architecture principles
 * by keeping business logic separate from presentation and data layers.
 */
interface LoginWithEmailUseCase {
    suspend operator fun invoke(email: String, password: String): Result<Unit>
}