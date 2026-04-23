package pt.dourobats.app.features.login.testing

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.api.usecase.VerifyLoginCodeUseCase

fun fakeVerifyLoginCodeUseCase(builder: FakeVerifyLoginCodeUseCase.() -> Unit = {}): VerifyLoginCodeUseCase =
    FakeVerifyLoginCodeUseCase().apply(builder).build()

class FakeVerifyLoginCodeUseCase {
    var invoke: suspend (email: String, code: String) -> Result<Unit> =
        { _, _ -> throw NotImplementedError() }

    fun build(): VerifyLoginCodeUseCase =
        object : VerifyLoginCodeUseCase {
            override suspend fun invoke(email: String, code: String): Result<Unit> =
                this@FakeVerifyLoginCodeUseCase.invoke(email, code)
        }
}
