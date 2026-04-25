package pt.dourobats.app.features.settings.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.features.settings.api.usecase.ObserveUserProfileUseCase
import pt.dourobats.app.features.login.api.model.UserProfile
import pt.dourobats.app.features.settings.repository.SettingsRepository

internal class ObserveUserProfileUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : ObserveUserProfileUseCase {
    override fun invoke(): Flow<UserProfile> = settingsRepository.userProfileFlow
}
