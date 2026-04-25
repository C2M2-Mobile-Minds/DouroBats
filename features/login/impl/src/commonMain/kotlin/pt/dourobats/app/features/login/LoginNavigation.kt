package pt.dourobats.app.features.login

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pt.dourobats.app.features.login.ui.LoginRoute as LoginRouteScreen

fun NavGraphBuilder.loginGraph() {
    composable<LoginRoute> {
        LoginRouteScreen()
    }
}
