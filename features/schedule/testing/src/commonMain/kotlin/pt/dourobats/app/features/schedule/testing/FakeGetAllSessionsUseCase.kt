package pt.dourobats.app.features.schedule.testing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import pt.dourobats.app.features.schedule.api.model.Session
import pt.dourobats.app.features.schedule.api.usecase.GetAllSessionsUseCase

class FakeGetAllSessionsUseCase : GetAllSessionsUseCase {
    var result: Flow<List<Session>> = flowOf(emptyList())

    override fun invoke(): Flow<List<Session>> = result
}
