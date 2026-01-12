package pt.dourobats.app.core.test.fakes

import pt.dourobats.app.core.domain.usecase.LogoutUseCase

class FakeLogoutUseCase : LogoutUseCase {
    var invoked = false
    override suspend fun invoke() {
        invoked = true
    }
}
