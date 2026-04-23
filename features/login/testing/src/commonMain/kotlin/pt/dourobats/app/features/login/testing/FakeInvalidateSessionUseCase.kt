package pt.dourobats.app.features.login.testing

import pt.dourobats.app.features.login.api.usecase.InvalidateSessionUseCase

fun fakeInvalidateSessionUseCase(builder: FakeInvalidateSessionUseCase.() -> Unit = {}): InvalidateSessionUseCase =
    FakeInvalidateSessionUseCase().apply(builder).build()

class FakeInvalidateSessionUseCase {
    var invoke: suspend () -> Unit = { throw NotImplementedError() }

    fun build(): InvalidateSessionUseCase =
        object : InvalidateSessionUseCase {
            override suspend fun invoke() = this@FakeInvalidateSessionUseCase.invoke()
        }
}
