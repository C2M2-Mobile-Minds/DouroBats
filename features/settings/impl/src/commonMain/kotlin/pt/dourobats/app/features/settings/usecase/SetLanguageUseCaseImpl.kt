package pt.dourobats.app.features.settings.usecase

import pt.dourobats.app.features.settings.api.usecase.SetLanguageUseCase
import pt.dourobats.app.core.localization.Language
import pt.dourobats.app.features.settings.repository.SettingsRepository

internal class SetLanguageUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : SetLanguageUseCase {
    override suspend fun invoke(language: Language) = settingsRepository.setLanguage(language)
}
