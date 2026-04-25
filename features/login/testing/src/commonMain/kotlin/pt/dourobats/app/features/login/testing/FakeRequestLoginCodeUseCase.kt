package pt.dourobats.app.features.login.testing

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.api.usecase.RequestLoginCodeUseCase

class FakeRequestLoginCodeUseCase : RequestLoginCodeUseCase {
    var result: Result<Unit> = Result.Success(Unit)
    var lastEmail: String? = null

    override suspend fun invoke(email: String): Result<Unit> {
        lastEmail = email
        return result
    }
}
