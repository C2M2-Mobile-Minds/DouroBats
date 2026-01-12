package pt.dourobats.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dourobats.core.ui.generated.resources.Res
import dourobats.core.ui.generated.resources.login_button_temp
import dourobats.core.ui.generated.resources.login_title
import dourobats.core.ui.generated.resources.nav_home
import dourobats.core.ui.generated.resources.nav_settings
import dourobats.core.ui.generated.resources.nav_training
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import pt.dourobats.app.core.model.AuthState
import pt.dourobats.app.core.model.Language
import pt.dourobats.app.core.model.Theme
import pt.dourobats.app.core.repository.AuthRepository
import pt.dourobats.app.core.repository.SettingsRepository
import pt.dourobats.app.core.ui.localization.LocalLanguage
import pt.dourobats.app.core.ui.localization.changeLanguage
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.features.home.HomeScreen
import pt.dourobats.app.features.schedule.ScheduleScreen
import pt.dourobats.app.features.settings.SettingsScreen

@Composable
@Preview
fun App() {
    AppContent()
}

@Composable
private fun AppContent() {
    val settingsRepository: SettingsRepository = koinInject()
    val authRepository: AuthRepository = koinInject()

    // Track if we've loaded preferences from DataStore
    var preferencesLoaded by remember { mutableStateOf(false) }

    // Load the saved language from DataStore (nullable to detect first load)
    val savedLanguage by settingsRepository.languageFlow.collectAsState(
        initial = null
    )

    // Load the saved theme from DataStore (nullable to detect first load)
    val savedTheme by settingsRepository.themeFlow.collectAsState(
        initial = null
    )

    // Reactive auth state - updates automatically when user logs in/out
    val authState by authRepository.authStateFlow.collectAsState(
        initial = AuthState.Loading
    )

    // Mark preferences as loaded once we have all values
    LaunchedEffect(savedLanguage, savedTheme, authState) {
        if (savedLanguage != null && savedTheme != null && authState !is AuthState.Loading) {
            preferencesLoaded = true
        }
    }

    // Initialize language when preferences load
    LaunchedEffect(savedLanguage) {
        savedLanguage?.let { changeLanguage(it) }
    }

    // Show empty box (native splash visible) until all preferences are loaded
    if (!preferencesLoaded) {
        Box(modifier = Modifier.fillMaxSize())
        return
    }

    // All preferences loaded - render app with correct settings
    val currentLanguage = savedLanguage ?: Language.ENGLISH_US
    val useDarkTheme = when (savedTheme ?: Theme.LIGHT) {
        Theme.LIGHT -> false
        Theme.DARK -> true
    }

    AppTheme(darkTheme = useDarkTheme) {
        CompositionLocalProvider(LocalLanguage provides currentLanguage) {
            when (authState) {
                is AuthState.Loading -> {
                    // Should not reach here since we check above, but handle gracefully
                    Box(modifier = Modifier.fillMaxSize())
                }
                is AuthState.Authenticated -> MainApp()
                is AuthState.Unauthenticated -> LoginScreen()
            }
        }
    }
}

@Composable
private fun MainApp() {
    var selectedScreen by remember { mutableStateOf(Screen.Home) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                Screen.entries.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = stringResource(screen.titleRes)) },
                        label = { Text(stringResource(screen.titleRes)) },
                        selected = selectedScreen == screen,
                        onClick = { selectedScreen = screen }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedScreen) {
                Screen.Home -> HomeScreen()
                Screen.Training -> ScheduleScreen()
                Screen.Settings -> SettingsScreen()
            }
        }
    }
}

@Composable
private fun LoginScreen() {
    pt.dourobats.app.features.login.LoginScreen()
}

private enum class Screen(val titleRes: org.jetbrains.compose.resources.StringResource, val icon: ImageVector) {
    Home(Res.string.nav_home, Icons.Default.Home),
    Training(Res.string.nav_training, Icons.Default.CalendarMonth),
    Settings(Res.string.nav_settings, Icons.Default.Settings)
}