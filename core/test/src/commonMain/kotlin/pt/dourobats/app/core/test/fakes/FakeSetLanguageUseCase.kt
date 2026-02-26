package pt.dourobats.app.core.test.fakes

import pt.dourobats.app.core.domain.usecase.SetLanguageUseCase
import pt.dourobats.app.core.model.Language

class FakeSetLanguageUseCase : SetLanguageUseCase {
    var lastLanguage: Language? = null
    var invokeCount = 0
    override suspend fun invoke(language: Language) {
        lastLanguage = language
        invokeCount++
    }
}
