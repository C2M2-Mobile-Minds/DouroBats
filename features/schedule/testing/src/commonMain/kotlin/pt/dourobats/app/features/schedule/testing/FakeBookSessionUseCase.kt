package pt.dourobats.app.features.schedule.testing

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.schedule.api.usecase.BookSessionUseCase

class FakeBookSessionUseCase : BookSessionUseCase {
    var result: Result<Unit> = Result.Success(Unit)
    var lastSessionId: String? = null

    override suspend fun invoke(sessionId: String): Result<Unit> {
        lastSessionId = sessionId
        return result
    }
}
