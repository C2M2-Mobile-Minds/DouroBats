package pt.dourobats.app.features.settings.usecase

import pt.dourobats.app.core.domain.usecase.SetThemeUseCase
import pt.dourobats.app.features.settings.api.model.Theme
import pt.dourobats.app.features.settings.repository.SettingsRepository

internal class SetThemeUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : SetThemeUseCase {
    override suspend fun invoke(theme: Theme) = settingsRepository.setTheme(theme)
}
