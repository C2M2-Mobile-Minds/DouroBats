package pt.dourobats.app.core.test.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import pt.dourobats.app.core.domain.usecase.ObserveUserProfileUseCase
import pt.dourobats.app.core.model.UserProfile

class FakeObserveUserProfileUseCase(
    initialProfile: UserProfile = UserProfile.empty()
) : ObserveUserProfileUseCase {
    val profileFlow = MutableStateFlow(initialProfile)
    override fun invoke(): Flow<UserProfile> = profileFlow
}
