package pt.dourobats.app.features.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import pt.dourobats.app.core.navigation.FeatureGraph
import pt.dourobats.app.features.settings.ui.NotificationsScreen
import pt.dourobats.app.features.settings.ui.SettingsRoute as SettingsRouteScreen

internal class SettingsGraph : FeatureGraph {
    override fun register(builder: NavGraphBuilder, navController: NavHostController) {
        builder.composable<SettingsRoute> {
            SettingsRouteScreen(
                onNavigateToNotifications = { navController.navigate(NotificationsRoute) }
            )
        }
        builder.composable<NotificationsRoute> {
            NotificationsScreen(onBack = { navController.popBackStack() })
        }
    }
}
