package pt.dourobats.app.features.schedule

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.scheduleGraph() {
    composable<ScheduleRoute> {
        ScheduleScreen()
    }
}

