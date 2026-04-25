package pt.dourobats.app.features.schedule

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pt.dourobats.app.features.schedule.ui.ScheduleRoute as ScheduleRouteScreen

fun NavGraphBuilder.scheduleGraph() {
    composable<ScheduleRoute> {
        ScheduleRouteScreen()
    }
}

