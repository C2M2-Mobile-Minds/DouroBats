package pt.dourobats.app.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.features.settings.api.model.Theme
import pt.dourobats.app.features.settings.api.repository.SettingsRepository

/**
 * Use case for observing the current theme setting as a continuous stream.
 */
interface ObserveThemeUseCase {
    operator fun invoke(): Flow<Theme>
}

class ObserveThemeUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : ObserveThemeUseCase {
    override fun invoke(): Flow<Theme> = settingsRepository.themeFlow
}
