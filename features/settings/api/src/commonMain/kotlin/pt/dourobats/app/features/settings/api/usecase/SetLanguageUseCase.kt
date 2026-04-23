package pt.dourobats.app.features.settings.api.usecase

import pt.dourobats.app.features.settings.api.model.Language

/**
 * Use case for persisting the user's language preference.
 */
interface SetLanguageUseCase {
    suspend operator fun invoke(language: Language)
}
