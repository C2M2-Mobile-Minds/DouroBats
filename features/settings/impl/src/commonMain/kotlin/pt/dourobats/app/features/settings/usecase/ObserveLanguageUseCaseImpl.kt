package pt.dourobats.app.features.settings.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.features.settings.api.usecase.ObserveLanguageUseCase
import pt.dourobats.app.core.localization.Language
import pt.dourobats.app.features.settings.repository.SettingsRepository

internal class ObserveLanguageUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : ObserveLanguageUseCase {
    override fun invoke(): Flow<Language> = settingsRepository.languageFlow
}
