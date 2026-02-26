package pt.dourobats.app.core.test.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import pt.dourobats.app.core.model.Session
import pt.dourobats.app.core.domain.usecase.GetAllSessionsUseCase

class FakeGetAllSessionsUseCase : GetAllSessionsUseCase {
    var sessions: List<Session> = emptyList()
    override fun invoke(): Flow<List<Session>> = flowOf(sessions)
}
