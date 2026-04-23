package pt.dourobats.app.features.login.api.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.features.login.api.model.AuthState

interface ObserveAuthStateUseCase {
    operator fun invoke(): Flow<AuthState>
}
