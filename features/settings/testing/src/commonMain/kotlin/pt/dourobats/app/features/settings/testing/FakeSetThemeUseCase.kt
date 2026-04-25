package pt.dourobats.app.features.settings.testing

import pt.dourobats.app.features.settings.api.model.Theme
import pt.dourobats.app.features.settings.api.usecase.SetThemeUseCase

class FakeSetThemeUseCase : SetThemeUseCase {
    var invocationCount: Int = 0
    var lastTheme: Theme? = null

    override suspend fun invoke(theme: Theme) {
        invocationCount++
        lastTheme = theme
    }
}
