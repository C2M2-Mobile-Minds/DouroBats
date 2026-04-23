package pt.dourobats.app.features.settings.testing

import pt.dourobats.app.core.localization.Language
import pt.dourobats.app.features.settings.api.usecase.SetLanguageUseCase

fun fakeSetLanguageUseCase(builder: FakeSetLanguageUseCase.() -> Unit = {}): SetLanguageUseCase =
    FakeSetLanguageUseCase().apply(builder).build()

class FakeSetLanguageUseCase {
    var invoke: suspend (language: Language) -> Unit = { _ -> throw NotImplementedError() }

    fun build(): SetLanguageUseCase =
        object : SetLanguageUseCase {
            override suspend fun invoke(language: Language) =
                this@FakeSetLanguageUseCase.invoke(language)
        }
}
