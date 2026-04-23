package pt.dourobats.app.features.settings.api.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.features.settings.api.model.Theme

/**
 * Use case for observing the current theme setting as a continuous stream.
 */
interface ObserveThemeUseCase {
    operator fun invoke(): Flow<Theme>
}
