package pt.dourobats.app.features.login.testing

import pt.dourobats.app.features.login.api.usecase.LogoutUseCase

fun fakeLogoutUseCase(builder: FakeLogoutUseCase.() -> Unit = {}): LogoutUseCase =
    FakeLogoutUseCase().apply(builder).build()

class FakeLogoutUseCase {
    var invoke: suspend () -> Unit = { throw NotImplementedError() }

    fun build(): LogoutUseCase =
        object : LogoutUseCase {
            override suspend fun invoke() = this@FakeLogoutUseCase.invoke()
        }
}
