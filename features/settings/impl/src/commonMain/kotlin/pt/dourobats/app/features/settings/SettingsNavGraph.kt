package pt.dourobats.app.features.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.settingsGraph() {
    composable<SettingsRoute> {
        SettingsScreen()
    }
}

