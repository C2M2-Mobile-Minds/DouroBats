package pt.dourobats.app.features.schedule.testing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate
import pt.dourobats.app.features.schedule.api.model.Session
import pt.dourobats.app.features.schedule.api.usecase.GetUserBookedSessionsUseCase

class FakeGetUserBookedSessionsUseCase : GetUserBookedSessionsUseCase {
    var result: Flow<List<Session>> = flowOf(emptyList())
    var lastDate: LocalDate? = null

    override fun invoke(fromDate: LocalDate): Flow<List<Session>> {
        lastDate = fromDate
        return result
    }
}
