package pt.dourobats.app.features.schedule.testing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate
import pt.dourobats.app.features.schedule.api.model.Session
import pt.dourobats.app.core.domain.usecase.GetAvailableSessionsUseCase

class FakeGetAvailableSessionsUseCase : GetAvailableSessionsUseCase {
    var sessions: List<Pair<Session, Boolean>> = emptyList()
    var lastDate: LocalDate? = null
    override fun invoke(date: LocalDate): Flow<List<Pair<Session, Boolean>>> {
        lastDate = date
        return flowOf(sessions)
    }
}
