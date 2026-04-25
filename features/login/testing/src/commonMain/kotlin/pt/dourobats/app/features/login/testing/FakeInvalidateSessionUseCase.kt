package pt.dourobats.app.features.login.testing

import pt.dourobats.app.features.login.api.usecase.InvalidateSessionUseCase

class FakeInvalidateSessionUseCase : InvalidateSessionUseCase {
    var invocationCount: Int = 0

    override suspend fun invoke() {
        invocationCount++
    }
}
