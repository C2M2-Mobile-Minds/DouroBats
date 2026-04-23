package pt.dourobats.app

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dourobats.core.ui.generated.resources.Res
import dourobats.core.ui.generated.resources.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import pt.dourobats.app.features.home.HomeRoute
import pt.dourobats.app.features.home.homeGraph
import pt.dourobats.app.features.login.LoginRoute
import pt.dourobats.app.features.login.loginGraph
import pt.dourobats.app.features.login.api.model.AuthState
import pt.dourobats.app.features.schedule.ScheduleRoute
import pt.dourobats.app.features.schedule.scheduleGraph
import pt.dourobats.app.features.settings.SettingsRoute
import pt.dourobats.app.features.settings.settingsGraph
import pt.dourobats.app.core.ui.localization.LocalLanguage
import pt.dourobats.app.core.ui.localization.changeLanguage
import pt.dourobats.app.core.ui.theme.AppTheme

@Composable
@Preview
fun App() {
    val viewModel: MainViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (!uiState.isReady) {
        Box(modifier = Modifier.fillMaxSize())
        return
    }

    remember(uiState.language) { changeLanguage(uiState.language) }

    AppTheme(darkTheme = uiState.useDarkTheme) {
        CompositionLocalProvider(LocalLanguage provides uiState.language) {
            DouroBatsNavigation(authState = uiState.authState)
        }
    }
}

@Composable
private fun DouroBatsNavigation(authState: AuthState) {
    val navController = rememberNavController()
    val startDestination = if (authState is AuthState.Authenticated) HomeRoute else LoginRoute

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                if (currentDestination != null && currentDestination.hasRoute(LoginRoute::class)) {
                    navController.navigate(HomeRoute) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                }
            }
            is AuthState.Unauthenticated -> {
                if (currentDestination != null && !currentDestination.hasRoute(LoginRoute::class)) {
                    navController.navigate(LoginRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
            is AuthState.Loading -> Unit
        }
    }

    val isOnAuthenticatedRoute = bottomNavItems.any {
        currentDestination?.hasRoute(it.route::class) == true
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (isOnAuthenticatedRoute) {
                AppBottomNavBar(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            loginGraph()
            homeGraph()
            scheduleGraph()
            settingsGraph()
        }
    }
}

private data class BottomNavItem(
    val route: Any,
    val titleRes: StringResource,
    val icon: ImageVector
)

private val bottomNavItems = listOf(
    BottomNavItem(HomeRoute, Res.string.nav_home, Icons.Default.Home),
    BottomNavItem(ScheduleRoute, Res.string.nav_training, Icons.Default.CalendarMonth),
    BottomNavItem(SettingsRoute, Res.string.nav_settings, Icons.Default.Settings),
)

@Composable
private fun AppBottomNavBar(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
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
                bottomNavItems.forEach { item ->
                    val isSelected = currentDestination?.hasRoute(item.route::class) == true
                    val contentColor = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    }

                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    val scale by animateFloatAsState(
                        targetValue = if (isPressed) 0.88f else 1f,
                        animationSpec = spring(dampingRatio = 0.6f, stiffness = 600f),
                        label = "navItemScale"
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .scale(scale)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo<HomeRoute> {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = stringResource(item.titleRes),
                            tint = contentColor,
                            modifier = Modifier.size(26.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = stringResource(item.titleRes),
                            color = contentColor,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(6.dp))

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
