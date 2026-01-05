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
import pt.dourobats.app.core.domain.model.Language
import pt.dourobats.app.core.domain.model.Theme
import pt.dourobats.app.core.domain.repository.SettingsRepository
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
    val systemDarkTheme = isSystemInDarkTheme()

    // Load the saved language from DataStore
    val savedLanguage by settingsRepository.languageFlow.collectAsState(
        initial = Language.ENGLISH_US
    )

    // Load the saved theme from DataStore
    val savedTheme by settingsRepository.themeFlow.collectAsState(
        initial = Theme.LIGHT
    )

    // Track language state for triggering recomposition
    var currentLanguage by remember { mutableStateOf(Language.ENGLISH_US) }

    // Update currentLanguage when savedLanguage changes
    LaunchedEffect(savedLanguage) {
        if (currentLanguage != savedLanguage) {
            currentLanguage = savedLanguage
            changeLanguage(savedLanguage)
        }
    }

    // Initialize locale on first composition
    LaunchedEffect(Unit) {
        changeLanguage(savedLanguage)
    }

    // Determine if dark theme should be used based on user preference
    val useDarkTheme = when (savedTheme) {
        Theme.LIGHT -> false
        Theme.DARK -> true
    }

    AppTheme(darkTheme = useDarkTheme) {
        // Provide the current language via CompositionLocal
        CompositionLocalProvider(LocalLanguage provides currentLanguage) {
            // TODO: Implement proper authentication flow with splash screen
            // For now, we'll show the main app directly
            var isAuthenticated by remember { mutableStateOf(true) }

            if (isAuthenticated) {
                MainApp()
            } else {
                LoginScreen(onLoginSuccess = { isAuthenticated = true })
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
private fun LoginScreen(onLoginSuccess: () -> Unit) {
    // TODO: Implement proper login UI
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(Res.string.login_title),
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onLoginSuccess) {
            Text(stringResource(Res.string.login_button_temp))
        }
    }
}

private enum class Screen(val titleRes: org.jetbrains.compose.resources.StringResource, val icon: ImageVector) {
    Home(Res.string.nav_home, Icons.Default.Home),
    Training(Res.string.nav_training, Icons.Default.CalendarMonth),
    Settings(Res.string.nav_settings, Icons.Default.Settings)
}