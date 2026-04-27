package pt.dourobats.app.features.management

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import pt.dourobats.app.features.management.ui.CreateSessionScreen
import pt.dourobats.app.features.management.ui.ManagementHubScreen

fun NavGraphBuilder.managementGraph(navController: NavHostController) {
    composable<ManagementHubRoute> {
        ManagementHubScreen(
            onActionSelected = { action ->
                when (action) {
                    ManagementAction.CREATE_SESSION -> navController.navigate(CreateSessionRoute)
                    ManagementAction.POST_ANNOUNCEMENT -> navController.popBackStack()
                    ManagementAction.CHECK_IN -> navController.popBackStack()
                }
            },
            onDismiss = { navController.popBackStack() },
        )
    }
    composable<CreateSessionRoute> {
        CreateSessionScreen(onBack = { navController.popBackStack() })
    }
}
