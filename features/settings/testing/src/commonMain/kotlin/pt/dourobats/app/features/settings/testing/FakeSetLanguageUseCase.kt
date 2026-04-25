package pt.dourobats.app.features.settings.testing

import pt.dourobats.app.core.localization.Language
import pt.dourobats.app.features.settings.api.usecase.SetLanguageUseCase

class FakeSetLanguageUseCase : SetLanguageUseCase {
    var invocationCount: Int = 0
    var lastLanguage: Language? = null

    override suspend fun invoke(language: Language) {
        invocationCount++
        lastLanguage = language
    }
}
