package pt.dourobats.app.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.features.schedule.api.model.Session

interface GetAllSessionsUseCase {
    operator fun invoke(): Flow<List<Session>>
}
