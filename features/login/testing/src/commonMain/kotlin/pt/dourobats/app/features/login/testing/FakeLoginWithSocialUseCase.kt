package pt.dourobats.app.features.login.testing

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.api.model.LoginMethod
import pt.dourobats.app.core.domain.usecase.LoginWithSocialUseCase

class FakeLoginWithSocialUseCase : LoginWithSocialUseCase {
    var result: Result<Unit> = Result.Success(Unit)
    var wasCalled = false
    var lastMethod: LoginMethod? = null

    override suspend fun invoke(method: LoginMethod): Result<Unit> {
        wasCalled = true
        lastMethod = method
        return result
    }
}
