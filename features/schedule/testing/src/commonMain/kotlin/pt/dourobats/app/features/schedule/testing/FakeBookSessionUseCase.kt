package pt.dourobats.app.features.schedule.testing

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.schedule.api.usecase.BookSessionUseCase

fun fakeBookSessionUseCase(builder: FakeBookSessionUseCase.() -> Unit = {}): BookSessionUseCase =
    FakeBookSessionUseCase().apply(builder).build()

class FakeBookSessionUseCase {
    var invoke: suspend (sessionId: String) -> Result<Unit> =
        { _ -> throw NotImplementedError() }

    fun build(): BookSessionUseCase =
        object : BookSessionUseCase {
            override suspend fun invoke(sessionId: String): Result<Unit> =
                this@FakeBookSessionUseCase.invoke(sessionId)
        }
}
