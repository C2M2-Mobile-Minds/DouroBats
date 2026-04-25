package pt.dourobats.app.features.login.testing

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.api.usecase.VerifyLoginCodeUseCase

class FakeVerifyLoginCodeUseCase : VerifyLoginCodeUseCase {
    var result: Result<Unit> = Result.Success(Unit)
    var lastEmail: String? = null
    var lastCode: String? = null

    override suspend fun invoke(email: String, code: String): Result<Unit> {
        lastEmail = email
        lastCode = code
        return result
    }
}
