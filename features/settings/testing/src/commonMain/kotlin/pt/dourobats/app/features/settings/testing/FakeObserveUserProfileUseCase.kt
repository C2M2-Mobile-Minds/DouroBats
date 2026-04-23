package pt.dourobats.app.features.settings.testing

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.features.login.api.model.UserProfile
import pt.dourobats.app.features.settings.api.usecase.ObserveUserProfileUseCase

fun fakeObserveUserProfileUseCase(builder: FakeObserveUserProfileUseCase.() -> Unit = {}): ObserveUserProfileUseCase =
    FakeObserveUserProfileUseCase().apply(builder).build()

class FakeObserveUserProfileUseCase {
    var invoke: () -> Flow<UserProfile> = { throw NotImplementedError() }

    fun build(): ObserveUserProfileUseCase =
        object : ObserveUserProfileUseCase {
            override fun invoke(): Flow<UserProfile> =
                this@FakeObserveUserProfileUseCase.invoke()
        }
}
