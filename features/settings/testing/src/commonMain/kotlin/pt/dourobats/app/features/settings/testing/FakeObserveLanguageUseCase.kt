package pt.dourobats.app.features.settings.testing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import pt.dourobats.app.core.localization.Language
import pt.dourobats.app.features.settings.api.usecase.ObserveLanguageUseCase

class FakeObserveLanguageUseCase : ObserveLanguageUseCase {
    var result: Flow<Language> = MutableStateFlow(Language.ENGLISH_US)

    override fun invoke(): Flow<Language> = result
}
