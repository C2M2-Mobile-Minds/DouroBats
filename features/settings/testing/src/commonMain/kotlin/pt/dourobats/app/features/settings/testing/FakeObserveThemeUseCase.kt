package pt.dourobats.app.features.settings.testing

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.features.settings.api.model.Theme
import pt.dourobats.app.features.settings.api.usecase.ObserveThemeUseCase

fun fakeObserveThemeUseCase(builder: FakeObserveThemeUseCase.() -> Unit = {}): ObserveThemeUseCase =
    FakeObserveThemeUseCase().apply(builder).build()

class FakeObserveThemeUseCase {
    var invoke: () -> Flow<Theme> = { throw NotImplementedError() }

    fun build(): ObserveThemeUseCase =
        object : ObserveThemeUseCase {
            override fun invoke(): Flow<Theme> =
                this@FakeObserveThemeUseCase.invoke()
        }
}
