package pt.dourobats.app.features.login.testing

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.api.usecase.RequestLoginCodeUseCase

fun fakeRequestLoginCodeUseCase(builder: FakeRequestLoginCodeUseCase.() -> Unit = {}): RequestLoginCodeUseCase =
    FakeRequestLoginCodeUseCase().apply(builder).build()

class FakeRequestLoginCodeUseCase {
    var invoke: suspend (email: String) -> Result<Unit> = { _ -> throw NotImplementedError() }

    fun build(): RequestLoginCodeUseCase =
        object : RequestLoginCodeUseCase {
            override suspend fun invoke(email: String): Result<Unit> =
                this@FakeRequestLoginCodeUseCase.invoke(email)
        }
}
