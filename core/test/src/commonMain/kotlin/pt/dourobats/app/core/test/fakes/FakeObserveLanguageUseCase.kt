package pt.dourobats.app.core.test.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import pt.dourobats.app.core.domain.usecase.ObserveLanguageUseCase
import pt.dourobats.app.core.model.Language

class FakeObserveLanguageUseCase(
    initialLanguage: Language = Language.ENGLISH_US
) : ObserveLanguageUseCase {
    val languageFlow = MutableStateFlow(initialLanguage)
    override fun invoke(): Flow<Language> = languageFlow
}
