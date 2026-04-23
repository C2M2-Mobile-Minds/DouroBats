package pt.dourobats.app.features.settings.testing

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.core.localization.Language
import pt.dourobats.app.features.settings.api.usecase.ObserveLanguageUseCase

fun fakeObserveLanguageUseCase(builder: FakeObserveLanguageUseCase.() -> Unit = {}): ObserveLanguageUseCase =
    FakeObserveLanguageUseCase().apply(builder).build()

class FakeObserveLanguageUseCase {
    var invoke: () -> Flow<Language> = { throw NotImplementedError() }

    fun build(): ObserveLanguageUseCase =
        object : ObserveLanguageUseCase {
            override fun invoke(): Flow<Language> =
                this@FakeObserveLanguageUseCase.invoke()
        }
}
