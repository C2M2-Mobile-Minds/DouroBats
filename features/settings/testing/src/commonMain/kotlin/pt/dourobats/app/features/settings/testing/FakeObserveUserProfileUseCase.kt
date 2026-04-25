package pt.dourobats.app.features.settings.testing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import pt.dourobats.app.features.login.api.model.UserProfile
import pt.dourobats.app.features.settings.api.usecase.ObserveUserProfileUseCase

class FakeObserveUserProfileUseCase : ObserveUserProfileUseCase {
    var result: Flow<UserProfile> = MutableStateFlow(UserProfile.empty())

    override fun invoke(): Flow<UserProfile> = result
}
