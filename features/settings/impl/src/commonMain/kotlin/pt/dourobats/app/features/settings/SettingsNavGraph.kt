package pt.dourobats.app.features.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pt.dourobats.app.features.settings.ui.SettingsRoute as SettingsRouteScreen

fun NavGraphBuilder.settingsGraph() {
    composable<SettingsRoute> {
        SettingsRouteScreen()
    }
}

