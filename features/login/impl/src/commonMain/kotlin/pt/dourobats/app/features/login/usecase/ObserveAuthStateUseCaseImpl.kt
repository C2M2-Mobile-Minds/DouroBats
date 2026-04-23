package pt.dourobats.app.features.login.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.core.domain.usecase.ObserveAuthStateUseCase
import pt.dourobats.app.features.login.api.model.AuthState
import pt.dourobats.app.features.login.repository.AuthRepository

internal class ObserveAuthStateUseCaseImpl(
    private val authRepository: AuthRepository
) : ObserveAuthStateUseCase {
    override fun invoke(): Flow<AuthState> = authRepository.authStateFlow
}
