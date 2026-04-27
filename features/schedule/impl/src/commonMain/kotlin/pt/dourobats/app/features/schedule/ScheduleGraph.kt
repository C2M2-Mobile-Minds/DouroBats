package pt.dourobats.app.features.schedule

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import pt.dourobats.app.core.navigation.FeatureGraph
import pt.dourobats.app.features.schedule.ui.ScheduleRoute as ScheduleRouteScreen

internal class ScheduleGraph : FeatureGraph {
    override fun register(builder: NavGraphBuilder, navController: NavHostController) {
        builder.composable<ScheduleRoute> {
            ScheduleRouteScreen()
        }
    }
}
