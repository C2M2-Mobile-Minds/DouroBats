package pt.dourobats.app.features.schedule.testing

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import pt.dourobats.app.features.schedule.api.model.Session
import pt.dourobats.app.features.schedule.api.usecase.GetAvailableSessionsUseCase

fun fakeGetAvailableSessionsUseCase(builder: FakeGetAvailableSessionsUseCase.() -> Unit = {}): GetAvailableSessionsUseCase =
    FakeGetAvailableSessionsUseCase().apply(builder).build()

class FakeGetAvailableSessionsUseCase {
    var invoke: (date: LocalDate) -> Flow<List<Pair<Session, Boolean>>> =
        { _ -> throw NotImplementedError() }

    fun build(): GetAvailableSessionsUseCase =
        object : GetAvailableSessionsUseCase {
            override fun invoke(date: LocalDate): Flow<List<Pair<Session, Boolean>>> =
                this@FakeGetAvailableSessionsUseCase.invoke(date)
        }
}
