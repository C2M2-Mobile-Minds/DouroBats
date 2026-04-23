package pt.dourobats.app.features.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.homeGraph() {
    composable<HomeRoute> {
        HomeRoute()
    }
}

