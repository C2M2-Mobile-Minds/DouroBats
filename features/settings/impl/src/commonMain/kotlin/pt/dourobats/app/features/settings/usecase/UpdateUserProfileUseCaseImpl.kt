package pt.dourobats.app.features.settings.usecase

import pt.dourobats.app.features.settings.api.usecase.UpdateUserProfileUseCase
import pt.dourobats.app.features.login.api.model.UserProfile
import pt.dourobats.app.features.settings.repository.SettingsRepository

internal class UpdateUserProfileUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : UpdateUserProfileUseCase {
    override suspend fun invoke(profile: UserProfile) = settingsRepository.updateUserProfile(profile)
}
