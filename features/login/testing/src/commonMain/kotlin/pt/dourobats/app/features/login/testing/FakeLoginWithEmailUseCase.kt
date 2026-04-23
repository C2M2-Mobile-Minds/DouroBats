package pt.dourobats.app.features.login.testing

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.api.usecase.LoginWithEmailUseCase

fun fakeLoginWithEmailUseCase(builder: FakeLoginWithEmailUseCase.() -> Unit = {}): LoginWithEmailUseCase =
    FakeLoginWithEmailUseCase().apply(builder).build()

class FakeLoginWithEmailUseCase {
    var invoke: suspend (email: String, password: String) -> Result<Unit> =
        { _, _ -> throw NotImplementedError() }

    fun build(): LoginWithEmailUseCase =
        object : LoginWithEmailUseCase {
            override suspend fun invoke(email: String, password: String): Result<Unit> =
                this@FakeLoginWithEmailUseCase.invoke(email, password)
        }
}
