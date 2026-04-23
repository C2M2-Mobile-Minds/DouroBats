package pt.dourobats.app.features.settings.api.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.features.settings.api.model.Language

/**
 * Use case for observing the current language setting as a continuous stream.
 */
interface ObserveLanguageUseCase {
    operator fun invoke(): Flow<Language>
}
