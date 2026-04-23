package pt.dourobats.app.features.schedule.testing

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.features.schedule.api.model.Session
import pt.dourobats.app.features.schedule.api.usecase.GetAllSessionsUseCase

fun fakeGetAllSessionsUseCase(builder: FakeGetAllSessionsUseCase.() -> Unit = {}): GetAllSessionsUseCase =
    FakeGetAllSessionsUseCase().apply(builder).build()

class FakeGetAllSessionsUseCase {
    var invoke: () -> Flow<List<Session>> = { throw NotImplementedError() }

    fun build(): GetAllSessionsUseCase =
        object : GetAllSessionsUseCase {
            override fun invoke(): Flow<List<Session>> =
                this@FakeGetAllSessionsUseCase.invoke()
        }
}
