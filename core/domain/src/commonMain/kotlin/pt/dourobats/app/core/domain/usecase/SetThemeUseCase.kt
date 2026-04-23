package pt.dourobats.app.core.domain.usecase

import pt.dourobats.app.features.settings.api.Theme
import pt.dourobats.app.features.settings.api.SettingsRepository

/**
 * Use case for persisting the user's theme preference.
 */
interface SetThemeUseCase {
    suspend operator fun invoke(theme: Theme)
}

class SetThemeUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : SetThemeUseCase {
    override suspend fun invoke(theme: Theme) = settingsRepository.setTheme(theme)
}
