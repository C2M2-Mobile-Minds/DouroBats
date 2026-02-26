package pt.dourobats.app.core.test.fakes

import pt.dourobats.app.core.domain.usecase.SetThemeUseCase
import pt.dourobats.app.core.model.Theme

class FakeSetThemeUseCase : SetThemeUseCase {
    var lastTheme: Theme? = null
    var invokeCount = 0
    override suspend fun invoke(theme: Theme) {
        lastTheme = theme
        invokeCount++
    }
}
