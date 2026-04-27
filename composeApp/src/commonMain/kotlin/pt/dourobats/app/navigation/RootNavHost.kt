package pt.dourobats.app.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import org.koin.core.qualifier.named
import pt.dourobats.app.core.navigation.FeatureGraph
import pt.dourobats.app.core.navigation.NavigationEvent
import pt.dourobats.app.core.navigation.NavigationManager
import pt.dourobats.app.features.home.HomeRoute
import pt.dourobats.app.features.login.LoginRoute
import pt.dourobats.app.features.login.api.model.AuthState
import pt.dourobats.app.navigation.components.AppBottomNavBar
import pt.dourobats.app.navigation.models.bottomNavItems

@Composable
internal fun RootNavHost(
    authState: AuthState,
    navController: NavHostController = rememberNavController(),
    navigationManager: NavigationManager = koinInject(),
) {
    val loginGraph: FeatureGraph = koinInject(named("login"))
    val homeGraph: FeatureGraph = koinInject(named("home"))
    val managementGraph: FeatureGraph = koinInject(named("management"))
    val scheduleGraph: FeatureGraph = koinInject(named("schedule"))
    val settingsGraph: FeatureGraph = koinInject(named("settings"))

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val startDestination = if (authState is AuthState.Authenticated) HomeRoute else LoginRoute

    HandleAuthTransitions(authState = authState, navController = navController)

    LaunchedEffect(Unit) {
        navigationManager.events.collect { event ->
            when (event) {
                is NavigationEvent.Navigate -> navController.navigate(event.route)
                NavigationEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }

    val showBottomBar = bottomNavItems.any {
        currentDestination?.hasRoute(it.route::class) == true
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically { it } + fadeIn(animationSpec = tween(200)),
                exit = slideOutVertically { it } + fadeOut(animationSpec = tween(200)),
            ) {
                AppBottomNavBar(navController = navController)
            }
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues),
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) },
        ) {
            loginGraph.register(this, navController)
            homeGraph.register(this, navController)
            managementGraph.register(this, navController)
            scheduleGraph.register(this, navController)
            settingsGraph.register(this, navController)
        }
    }
}

@Composable
private fun HandleAuthTransitions(
    authState: AuthState,
    navController: NavHostController,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                if (currentDestination?.hasRoute(LoginRoute::class) == true) {
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
}
