package pt.dourobats.app.features.settings.testing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import pt.dourobats.app.features.settings.api.usecase.ObserveLanguageUseCase
import pt.dourobats.app.features.settings.api.model.Language

class FakeObserveLanguageUseCase(
    initialLanguage: Language = Language.ENGLISH_US
) : ObserveLanguageUseCase {
    val languageFlow = MutableStateFlow(initialLanguage)
    override fun invoke(): Flow<Language> = languageFlow
}
