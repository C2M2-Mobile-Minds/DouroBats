package pt.dourobats.app.features.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import pt.dourobats.app.core.navigation.FeatureGraph
import pt.dourobats.app.features.home.ui.HomeRoute as HomeRouteComposable

internal class HomeGraph : FeatureGraph {
    override fun register(builder: NavGraphBuilder, navController: NavHostController) {
        builder.composable<HomeRoute> {
            HomeRouteComposable(savedStateHandle = it.savedStateHandle)
        }
    }
}
