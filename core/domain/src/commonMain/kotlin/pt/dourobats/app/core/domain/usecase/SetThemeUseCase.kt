package pt.dourobats.app.core.domain.usecase

import pt.dourobats.app.features.settings.api.model.Theme

/**
 * Use case for persisting the user's theme preference.
 */
interface SetThemeUseCase {
    suspend operator fun invoke(theme: Theme)
}
