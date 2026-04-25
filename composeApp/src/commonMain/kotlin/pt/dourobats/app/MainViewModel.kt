package pt.dourobats.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import pt.dourobats.app.features.login.api.usecase.ObserveAuthStateUseCase
import pt.dourobats.app.features.settings.api.usecase.ObserveLanguageUseCase
import pt.dourobats.app.features.settings.api.usecase.ObserveThemeUseCase

internal class MainViewModel(
    observeAuthState: ObserveAuthStateUseCase,
    observeLanguage: ObserveLanguageUseCase,
    observeTheme: ObserveThemeUseCase,
) : ViewModel() {

    val uiState: StateFlow<MainUiState> = combine(
        observeAuthState(),
        observeLanguage(),
        observeTheme(),
    ) { authState, language, theme ->
        MainUiState(authState = authState, language = language, theme = theme)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MainUiState(),
    )
}
