package pt.dourobats.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import pt.dourobats.app.core.localization.LocalLanguage
import pt.dourobats.app.core.localization.changeLanguage
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.navigation.RootNavHost

@Composable
@Preview
fun App() {
    val viewModel: MainViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (!uiState.isReady) {
        return
    }

    remember(uiState.language) { changeLanguage(uiState.language) }

    AppTheme(darkTheme = uiState.useDarkTheme) {
        CompositionLocalProvider(LocalLanguage provides uiState.language) {
            RootNavHost(authState = uiState.authState)
        }
    }
}

