package pt.dourobats.app.features.login

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import pt.dourobats.app.core.navigation.FeatureGraph
import pt.dourobats.app.features.login.ui.LoginRoute as LoginRouteScreen

internal class LoginGraph : FeatureGraph {
    override fun register(builder: NavGraphBuilder, navController: NavHostController) {
        builder.composable<LoginRoute> {
            LoginRouteScreen()
        }
    }
}
