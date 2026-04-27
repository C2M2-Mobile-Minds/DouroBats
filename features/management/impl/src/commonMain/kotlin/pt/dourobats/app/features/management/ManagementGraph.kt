package pt.dourobats.app.features.management

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import pt.dourobats.app.core.navigation.FeatureGraph
import pt.dourobats.app.features.management.ui.CreateSessionScreen
import pt.dourobats.app.features.management.ui.ManagementHubScreen
import pt.dourobats.app.features.management.ui.ManagementViewModel

internal class ManagementGraph : FeatureGraph {
    override fun register(builder: NavGraphBuilder, navController: NavHostController) {
        builder.composable<ManagementHubRoute> {
            val viewModel: ManagementViewModel = koinViewModel()

            LaunchedEffect(Unit) {
                viewModel.effects.collect { effect ->
                    when (effect) {
                        ManagementEffect.NavigateToCreateSession -> navController.navigate(CreateSessionRoute)
                        ManagementEffect.NavigateToAnnouncement -> navController.popBackStack()
                        ManagementEffect.NavigateToCheckIn -> navController.popBackStack()
                        ManagementEffect.Back -> navController.popBackStack()
                    }
                }
            }

            ManagementHubScreen(
                onAction = viewModel::onAction,
                onDismiss = { viewModel.onAction(ManagementAction.Dismiss) },
            )
        }
        builder.composable<CreateSessionRoute> {
            CreateSessionScreen(onBack = { navController.popBackStack() })
        }
    }
}
