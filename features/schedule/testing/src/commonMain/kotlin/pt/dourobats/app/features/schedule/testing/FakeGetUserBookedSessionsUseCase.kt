package pt.dourobats.app.features.schedule.testing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate
import pt.dourobats.app.features.schedule.api.model.Session
import pt.dourobats.app.core.domain.usecase.GetUserBookedSessionsUseCase

class FakeGetUserBookedSessionsUseCase : GetUserBookedSessionsUseCase {
    var sessions: List<Session> = emptyList()
    var lastFromDate: LocalDate? = null
    override fun invoke(fromDate: LocalDate): Flow<List<Session>> {
        lastFromDate = fromDate
        return flowOf(sessions.filter { it.dateTime.date >= fromDate })
    }
}
