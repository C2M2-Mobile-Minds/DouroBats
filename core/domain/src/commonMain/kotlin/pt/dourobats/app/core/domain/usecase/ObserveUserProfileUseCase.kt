package pt.dourobats.app.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.core.model.UserProfile
import pt.dourobats.app.core.repository.SettingsRepository

/**
 * Use case for observing the current user profile as a continuous stream.
 */
interface ObserveUserProfileUseCase {
    operator fun invoke(): Flow<UserProfile>
}

class ObserveUserProfileUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : ObserveUserProfileUseCase {
    override fun invoke(): Flow<UserProfile> = settingsRepository.userProfileFlow
}
