package pt.dourobats.app.core.test.fakes

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.core.model.LoginMethod
import pt.dourobats.app.core.domain.usecase.LoginWithSocialUseCase

class FakeLoginWithSocialUseCase : LoginWithSocialUseCase {
    var shouldFail: Boolean = false
    var lastMethod: LoginMethod? = null
    override suspend fun invoke(method: LoginMethod): Result<Unit> {
        lastMethod = method
        return if (shouldFail) {
            Result.Error(IllegalArgumentException("Fake failure"))
        } else {
            Result.Success(Unit)
        }
    }
}
