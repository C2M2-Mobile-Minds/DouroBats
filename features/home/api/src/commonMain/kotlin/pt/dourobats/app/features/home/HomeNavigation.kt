package pt.dourobats.app.features.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pt.dourobats.app.features.home.ui.HomeRoute as HomeRouteComposable

fun NavGraphBuilder.homeGraph(
    onCreateSession: () -> Unit = {},
    onViewReports: () -> Unit = {},
    onManageMembers: () -> Unit = {},
    onNewsDetail: (String) -> Unit = {},
) {
    composable<HomeRoute> {
        HomeRouteComposable(
            onCreateSession = onCreateSession,
            onViewReports = onViewReports,
            onManageMembers = onManageMembers,
            onNewsDetail = onNewsDetail,
        )
    }
}
