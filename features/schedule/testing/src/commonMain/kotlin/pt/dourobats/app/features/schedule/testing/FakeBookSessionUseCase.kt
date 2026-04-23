package pt.dourobats.app.features.schedule.testing

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.core.common.exception.ValidationException
import pt.dourobats.app.features.schedule.api.usecase.BookSessionUseCase

class FakeBookSessionUseCase : BookSessionUseCase {
    var shouldFail: Boolean = false
    var lastSessionId: String? = null

    override suspend fun invoke(sessionId: String): Result<Unit> {
        lastSessionId = sessionId
        return if (shouldFail || sessionId.isBlank()) {
            Result.Error(ValidationException.RequiredField("Session ID"))
        } else {
            Result.Success(Unit)
        }
    }
}
