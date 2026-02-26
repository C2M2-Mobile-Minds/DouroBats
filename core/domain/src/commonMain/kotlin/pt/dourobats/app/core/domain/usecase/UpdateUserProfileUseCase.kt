package pt.dourobats.app.core.domain.usecase

import pt.dourobats.app.core.model.UserProfile
import pt.dourobats.app.core.repository.SettingsRepository

/**
 * Use case for saving an updated user profile.
 */
interface UpdateUserProfileUseCase {
    suspend operator fun invoke(profile: UserProfile)
}

class UpdateUserProfileUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : UpdateUserProfileUseCase {
    override suspend fun invoke(profile: UserProfile) = settingsRepository.updateUserProfile(profile)
}
