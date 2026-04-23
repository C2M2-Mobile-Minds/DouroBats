package pt.dourobats.app

import pt.dourobats.app.features.login.api.model.AuthState
import pt.dourobats.app.features.settings.api.model.Language
import pt.dourobats.app.features.settings.api.model.Theme

internal data class MainUiState(
    val authState: AuthState = AuthState.Loading,
    val language: Language = Language.ENGLISH_US,
    val theme: Theme = Theme.LIGHT,
) {
    val isReady: Boolean get() = authState !is AuthState.Loading
    val useDarkTheme: Boolean get() = theme == Theme.DARK
}
