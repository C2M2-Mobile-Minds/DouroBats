package pt.dourobats.app.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface FeatureGraph {
    fun register(builder: NavGraphBuilder, navController: NavHostController)
}
