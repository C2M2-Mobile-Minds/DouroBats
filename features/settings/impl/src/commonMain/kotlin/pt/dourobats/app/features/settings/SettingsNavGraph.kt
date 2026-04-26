package pt.dourobats.app.features.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pt.dourobats.app.features.settings.ui.NotificationsScreen
import pt.dourobats.app.features.settings.ui.SettingsRoute as SettingsRouteScreen

fun NavGraphBuilder.settingsGraph(navController: NavController) {
    composable<SettingsRoute> {
        SettingsRouteScreen(
            onNavigateToNotifications = { navController.navigate(NotificationsRoute) }
        )
    }
    composable<NotificationsRoute> {
        NotificationsScreen(onBack = { navController.popBackStack() })
    }
}

