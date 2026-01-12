package pt.dourobats.app.core.test.fakes

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.core.model.LoginMethod
import pt.dourobats.app.core.domain.usecase.LoginWithSocialUseCase

class FakeLoginWithSocialUseCase : LoginWithSocialUseCase {
    // Control properties to simulate different outcomes
    var result: Result<Unit> = Result.Success(Unit)

    // Verification properties to check what the ViewModel passed
    var wasCalled = false
    var lastMethod: LoginMethod? = null

    override suspend fun invoke(method: LoginMethod): Result<Unit> {
        wasCalled = true
        lastMethod = method
        return result
    }
}
