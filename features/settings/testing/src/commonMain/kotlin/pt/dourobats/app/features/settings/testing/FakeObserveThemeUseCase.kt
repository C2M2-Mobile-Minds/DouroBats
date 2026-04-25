package pt.dourobats.app.features.settings.testing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import pt.dourobats.app.features.settings.api.model.Theme
import pt.dourobats.app.features.settings.api.usecase.ObserveThemeUseCase

class FakeObserveThemeUseCase : ObserveThemeUseCase {
    var result: Flow<Theme> = MutableStateFlow(Theme.LIGHT)

    override fun invoke(): Flow<Theme> = result
}
