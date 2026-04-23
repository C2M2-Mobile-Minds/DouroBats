package pt.dourobats.app.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.features.login.api.model.AuthState

interface ObserveAuthStateUseCase {
    operator fun invoke(): Flow<AuthState>
}
