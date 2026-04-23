package pt.dourobats.app.core.domain.usecase

import pt.dourobats.app.features.settings.api.Language
import pt.dourobats.app.features.settings.api.SettingsRepository

/**
 * Use case for persisting the user's language preference.
 */
interface SetLanguageUseCase {
    suspend operator fun invoke(language: Language)
}

class SetLanguageUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : SetLanguageUseCase {
    override suspend fun invoke(language: Language) = settingsRepository.setLanguage(language)
}
