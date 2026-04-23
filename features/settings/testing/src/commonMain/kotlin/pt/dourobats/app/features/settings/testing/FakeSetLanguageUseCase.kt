package pt.dourobats.app.features.settings.testing

import pt.dourobats.app.features.settings.api.usecase.SetLanguageUseCase
import pt.dourobats.app.features.settings.api.model.Language

class FakeSetLanguageUseCase : SetLanguageUseCase {
    var lastLanguage: Language? = null
    var invokeCount = 0
    override suspend fun invoke(language: Language) {
        lastLanguage = language
        invokeCount++
    }
}
