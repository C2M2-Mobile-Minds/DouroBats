package pt.dourobats.app.features.settings.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.features.settings.api.usecase.ObserveThemeUseCase
import pt.dourobats.app.features.settings.api.model.Theme
import pt.dourobats.app.features.settings.repository.SettingsRepository

internal class ObserveThemeUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : ObserveThemeUseCase {
    override fun invoke(): Flow<Theme> = settingsRepository.themeFlow
}
