package pt.dourobats.app.features.settings.testing

import pt.dourobats.app.core.domain.usecase.SetThemeUseCase
import pt.dourobats.app.features.settings.api.Theme

class FakeSetThemeUseCase : SetThemeUseCase {
    var lastTheme: Theme? = null
    var invokeCount = 0
    override suspend fun invoke(theme: Theme) {
        lastTheme = theme
        invokeCount++
    }
}
