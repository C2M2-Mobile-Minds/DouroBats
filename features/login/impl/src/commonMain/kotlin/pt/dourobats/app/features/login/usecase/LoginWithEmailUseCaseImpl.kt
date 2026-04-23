package pt.dourobats.app.features.login.usecase

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.core.common.exception.ValidationException
import pt.dourobats.app.core.domain.usecase.LoginWithEmailUseCase
import pt.dourobats.app.features.login.repository.AuthRepository

internal class LoginWithEmailUseCaseImpl(
    private val authRepository: AuthRepository
) : LoginWithEmailUseCase {

    override suspend fun invoke(email: String, password: String): Result<Unit> {
        val trimmedEmail = email.trim()

        if (trimmedEmail.isBlank()) {
            return Result.Error(ValidationException.RequiredField("Email"))
        }

        if (!isValidEmailFormat(trimmedEmail)) {
            return Result.Error(ValidationException.InvalidEmail("Invalid format"))
        }

        if (password.isBlank()) {
            return Result.Error(ValidationException.RequiredField("Password"))
        }

        if (password.length < MIN_PASSWORD_LENGTH) {
            return Result.Error(ValidationException.InvalidPassword("Too short"))
        }

        return authRepository.loginWithEmail(trimmedEmail, password)
    }

    private fun isValidEmailFormat(email: String): Boolean {
        return EMAIL_REGEX.matches(email)
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        private const val MIN_PASSWORD_LENGTH = 6
    }
}
