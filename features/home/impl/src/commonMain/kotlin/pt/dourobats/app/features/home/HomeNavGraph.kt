package pt.dourobats.app.features.home

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.homeGraph() {
    composable<HomeRoute> {
        val viewModel: HomeViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsState()
        HomeScreen(
            isCommitteeUser = uiState.isCommitteeUser,
            displayName = uiState.displayName
        )
    }
}

