package pt.dourobats.app.features.schedule.testing

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import pt.dourobats.app.features.schedule.api.model.Session
import pt.dourobats.app.features.schedule.api.usecase.GetUserBookedSessionsUseCase

fun fakeGetUserBookedSessionsUseCase(builder: FakeGetUserBookedSessionsUseCase.() -> Unit = {}): GetUserBookedSessionsUseCase =
    FakeGetUserBookedSessionsUseCase().apply(builder).build()

class FakeGetUserBookedSessionsUseCase {
    var invoke: (fromDate: LocalDate) -> Flow<List<Session>> =
        { _ -> throw NotImplementedError() }

    fun build(): GetUserBookedSessionsUseCase =
        object : GetUserBookedSessionsUseCase {
            override fun invoke(fromDate: LocalDate): Flow<List<Session>> =
                this@FakeGetUserBookedSessionsUseCase.invoke(fromDate)
        }
}
