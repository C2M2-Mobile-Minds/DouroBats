package pt.dourobats.app.features.settings.testing

import pt.dourobats.app.features.settings.api.model.Theme
import pt.dourobats.app.features.settings.api.usecase.SetThemeUseCase

fun fakeSetThemeUseCase(builder: FakeSetThemeUseCase.() -> Unit = {}): SetThemeUseCase =
    FakeSetThemeUseCase().apply(builder).build()

class FakeSetThemeUseCase {
    var invoke: suspend (theme: Theme) -> Unit = { _ -> throw NotImplementedError() }

    fun build(): SetThemeUseCase =
        object : SetThemeUseCase {
            override suspend fun invoke(theme: Theme) =
                this@FakeSetThemeUseCase.invoke(theme)
        }
}
