package pt.dourobats.app.features.settings.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.core.domain.usecase.ObserveLanguageUseCase
import pt.dourobats.app.features.settings.api.model.Language
import pt.dourobats.app.features.settings.repository.SettingsRepository

internal class ObserveLanguageUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : ObserveLanguageUseCase {
    override fun invoke(): Flow<Language> = settingsRepository.languageFlow
}
