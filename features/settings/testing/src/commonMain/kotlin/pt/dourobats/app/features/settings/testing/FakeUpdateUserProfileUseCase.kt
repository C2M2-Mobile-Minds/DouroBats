package pt.dourobats.app.features.settings.testing

import pt.dourobats.app.features.login.api.model.UserProfile
import pt.dourobats.app.features.settings.api.usecase.UpdateUserProfileUseCase

class FakeUpdateUserProfileUseCase : UpdateUserProfileUseCase {
    var invocationCount: Int = 0
    var lastProfile: UserProfile? = null

    override suspend fun invoke(profile: UserProfile) {
        invocationCount++
        lastProfile = profile
    }
}
