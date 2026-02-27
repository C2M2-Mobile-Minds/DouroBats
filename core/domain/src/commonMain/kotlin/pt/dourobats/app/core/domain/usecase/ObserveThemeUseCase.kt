package pt.dourobats.app.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.core.model.Theme
import pt.dourobats.app.core.repository.SettingsRepository

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
