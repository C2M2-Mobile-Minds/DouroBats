package pt.dourobats.app.features.management

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import org.koin.compose.viewmodel.koinViewModel
import pt.dourobats.app.core.navigation.FeatureGraph
import pt.dourobats.app.features.management.ui.CreateSessionEffect
import pt.dourobats.app.features.management.ui.CreateSessionScreen
import pt.dourobats.app.features.management.ui.CreateSessionViewModel
import pt.dourobats.app.features.management.ui.ManagementHubScreen
import pt.dourobats.app.features.management.ui.ManagementViewModel

internal class ManagementGraph : FeatureGraph {
    override fun register(builder: NavGraphBuilder, navController: NavHostController) {
        builder.dialog<ManagementHubRoute> {
            val viewModel: ManagementViewModel = koinViewModel()

            LaunchedEffect(Unit) {
                viewModel.effects.collect { effect ->
                    when (effect) {
                        ManagementEffect.NavigateToCreateSession -> navController.navigate(CreateSessionRoute)
                        ManagementEffect.NavigateToAnnouncement -> navController.popBackStack()
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
            val viewModel: CreateSessionViewModel = koinViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.effects.collect { effect ->
                    when (effect) {
                        is CreateSessionEffect.SessionCreated -> {
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("session_created_name", effect.sessionName)
                            navController.popBackStack()
                        }
                        CreateSessionEffect.NavigateBack -> navController.popBackStack()
                    }
                }
            }

            CreateSessionScreen(
                uiState = uiState,
                onAction = viewModel::onAction,
            )
        }
    }
}
