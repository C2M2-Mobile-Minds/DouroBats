package pt.dourobats.app.features.login.testing

import pt.dourobats.app.features.login.api.usecase.LogoutUseCase

class FakeLogoutUseCase : LogoutUseCase {
    var invoked = false
    override suspend fun invoke() {
        invoked = true
    }
}
