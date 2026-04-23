package pt.dourobats.app.features.settings.testing

import pt.dourobats.app.features.login.api.model.UserProfile
import pt.dourobats.app.features.settings.api.usecase.UpdateUserProfileUseCase

fun fakeUpdateUserProfileUseCase(builder: FakeUpdateUserProfileUseCase.() -> Unit = {}): UpdateUserProfileUseCase =
    FakeUpdateUserProfileUseCase().apply(builder).build()

class FakeUpdateUserProfileUseCase {
    var invoke: suspend (profile: UserProfile) -> Unit = { _ -> throw NotImplementedError() }

    fun build(): UpdateUserProfileUseCase =
        object : UpdateUserProfileUseCase {
            override suspend fun invoke(profile: UserProfile) =
                this@FakeUpdateUserProfileUseCase.invoke(profile)
        }
}
