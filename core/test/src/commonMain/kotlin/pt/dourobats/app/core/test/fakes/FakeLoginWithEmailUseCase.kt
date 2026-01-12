package pt.dourobats.app.core.test.fakes

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.core.common.exception.ValidationException
import pt.dourobats.app.core.domain.usecase.LoginWithEmailUseCase

class FakeLoginWithEmailUseCase : LoginWithEmailUseCase {
    // Control properties to simulate different outcomes
    var result: Result<Unit> = Result.Success(Unit)

    // Verification properties to check what the ViewModel passed
    var lastEmail: String? = null
    var lastPassword: String? = null

    override suspend fun invoke(email: String, password: String): Result<Unit> {
        lastEmail = email
        lastPassword = password
        return result
    }
}
