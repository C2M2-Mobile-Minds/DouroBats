package pt.dourobats.app.features.settings.testing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import pt.dourobats.app.features.settings.api.usecase.ObserveUserProfileUseCase
import pt.dourobats.app.features.login.api.model.UserProfile

class FakeObserveUserProfileUseCase(
    initialProfile: UserProfile = UserProfile.empty()
) : ObserveUserProfileUseCase {
    val profileFlow = MutableStateFlow(initialProfile)
    override fun invoke(): Flow<UserProfile> = profileFlow
}
