package pt.dourobats.app.features.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pt.dourobats.app.features.home.ui.HomeRoute as HomeRouteScreen

fun NavGraphBuilder.homeGraph() {
    composable<HomeRoute> {
        HomeRouteScreen()
    }
}