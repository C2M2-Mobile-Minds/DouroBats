package pt.dourobats.app.features.login.testing

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.api.usecase.LoginWithEmailUseCase

class FakeLoginWithEmailUseCase : LoginWithEmailUseCase {
    var result: Result<Unit> = Result.Success(Unit)
    var wasCalled = false
    var lastEmail: String? = null
    var lastPassword: String? = null

    override suspend fun invoke(email: String, password: String): Result<Unit> {
        wasCalled = true
        lastEmail = email
        lastPassword = password
        return result
    }
}
