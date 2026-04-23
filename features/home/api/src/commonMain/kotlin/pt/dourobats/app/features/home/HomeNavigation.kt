package pt.dourobats.app.features.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pt.dourobats.app.features.home.ui.HomeRoute as HomeRouteComposable

fun NavGraphBuilder.homeGraph() {
    composable<HomeRoute> {
        HomeRouteComposable()
    }
}
