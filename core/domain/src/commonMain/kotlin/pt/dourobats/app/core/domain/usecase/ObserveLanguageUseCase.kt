package pt.dourobats.app.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.features.settings.api.model.Language
import pt.dourobats.app.features.settings.api.repository.SettingsRepository

/**
 * Use case for observing the current language setting as a continuous stream.
 */
interface ObserveLanguageUseCase {
    operator fun invoke(): Flow<Language>
}

class ObserveLanguageUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : ObserveLanguageUseCase {
    override fun invoke(): Flow<Language> = settingsRepository.languageFlow
}
