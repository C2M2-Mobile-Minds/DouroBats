package pt.dourobats.app.features.settings.testing

import pt.dourobats.app.core.domain.usecase.UpdateUserProfileUseCase
import pt.dourobats.app.features.login.api.model.UserProfile

class FakeUpdateUserProfileUseCase : UpdateUserProfileUseCase {
    var lastProfile: UserProfile? = null
    var invokeCount = 0
    override suspend fun invoke(profile: UserProfile) {
        lastProfile = profile
        invokeCount++
    }
}
