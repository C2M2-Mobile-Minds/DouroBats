package pt.dourobats.app.features.login.testing

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.api.model.LoginMethod
import pt.dourobats.app.features.login.api.usecase.LoginWithSocialUseCase

fun fakeLoginWithSocialUseCase(builder: FakeLoginWithSocialUseCase.() -> Unit = {}): LoginWithSocialUseCase =
    FakeLoginWithSocialUseCase().apply(builder).build()

class FakeLoginWithSocialUseCase {
    var invoke: suspend (method: LoginMethod) -> Result<Unit> =
        { _ -> throw NotImplementedError() }

    fun build(): LoginWithSocialUseCase =
        object : LoginWithSocialUseCase {
            override suspend fun invoke(method: LoginMethod): Result<Unit> =
                this@FakeLoginWithSocialUseCase.invoke(method)
        }
}
