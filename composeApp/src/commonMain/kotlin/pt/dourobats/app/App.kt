package pt.dourobats.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dourobats.core.ui.generated.resources.Res
import dourobats.core.ui.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import pt.dourobats.app.core.model.AuthState
import pt.dourobats.app.core.model.Language
import pt.dourobats.app.core.model.Theme
import pt.dourobats.app.core.repository.AuthRepository
import pt.dourobats.app.core.repository.SettingsRepository
import pt.dourobats.app.core.ui.localization.LocalLanguage
import pt.dourobats.app.core.ui.localization.changeLanguage
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.features.home.HomeScreen
import pt.dourobats.app.features.home.HomeViewModel
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

    // Hoisted above all conditionals — survives any auth state re-emission
    var selectedScreen by remember { mutableStateOf(Screen.Home) }
    var preferencesLoaded by remember { mutableStateOf(false) }

    val savedLanguage by settingsRepository.languageFlow.collectAsState(initial = null)
    val savedTheme by settingsRepository.themeFlow.collectAsState(initial = null)
    val authState by authRepository.authStateFlow.collectAsState(initial = AuthState.Loading)

    LaunchedEffect(savedLanguage, savedTheme, authState) {
        if (savedLanguage != null && savedTheme != null && authState !is AuthState.Loading) {
            preferencesLoaded = true
        }
    }

    if (!preferencesLoaded) {
        Box(modifier = Modifier.fillMaxSize())
        return
    }

    val currentLanguage = savedLanguage ?: Language.ENGLISH_US
    val useDarkTheme = when (savedTheme ?: Theme.LIGHT) {
        Theme.LIGHT -> false
        Theme.DARK -> true
    }

    remember(currentLanguage) { changeLanguage(currentLanguage) }

    AppTheme(darkTheme = useDarkTheme) {
        CompositionLocalProvider(LocalLanguage provides currentLanguage) {
            when (authState) {
                is AuthState.Loading -> Box(modifier = Modifier.fillMaxSize())
                is AuthState.Authenticated -> MainApp(
                    selectedScreen = selectedScreen,
                    onScreenSelected = { selectedScreen = it }
                )
                is AuthState.Unauthenticated -> LoginScreen()
            }
        }
    }
}

@Composable
private fun MainApp(
    selectedScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            CustomNavigationBar(
                selectedScreen = selectedScreen,
                onScreenSelected = { onScreenSelected(it) }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedScreen) {
                Screen.Home -> {
                    val homeViewModel = koinViewModel<HomeViewModel>()
                    val homeUiState by homeViewModel.uiState.collectAsState()
                    HomeScreen(isCommitteeUser = homeUiState.isCommitteeUser, userName = homeUiState.userName)
                }
                Screen.Training -> ScheduleScreen()
                Screen.Settings -> SettingsScreen()
            }
        }
    }
}

@Composable
private fun CustomNavigationBar(
    selectedScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding() // Ensures it sits above system buttons
            .padding(horizontal = 16.dp, vertical = 12.dp) // Adjusted vertical padding
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            shadowElevation = 12.dp
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Screen.entries.forEach { screen ->
                    val isSelected = selectedScreen == screen
                    val contentColor = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { onScreenSelected(screen) }
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = stringResource(screen.titleRes),
                            tint = contentColor,
                            modifier = Modifier.size(26.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = stringResource(screen.titleRes),
                            color = contentColor,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                        
                        Spacer(modifier = Modifier.height(6.dp))
                        
                        // Selected indicator line
                        Box(
                            modifier = Modifier
                                .width(16.dp)
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                        )
                    }
                }
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
