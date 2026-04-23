package pt.dourobats.app.features.settings.testing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import pt.dourobats.app.core.domain.usecase.ObserveThemeUseCase
import pt.dourobats.app.features.settings.api.Theme

class FakeObserveThemeUseCase(
    initialTheme: Theme = Theme.LIGHT
) : ObserveThemeUseCase {
    val themeFlow = MutableStateFlow(initialTheme)
    override fun invoke(): Flow<Theme> = themeFlow
}
