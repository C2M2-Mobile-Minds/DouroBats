package pt.dourobats.app.navigation.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import dourobats.core.ui.generated.resources.Res
import dourobats.core.ui.generated.resources.nav_home
import dourobats.core.ui.generated.resources.nav_settings
import dourobats.core.ui.generated.resources.nav_training
import org.jetbrains.compose.resources.StringResource
import pt.dourobats.app.features.home.HomeRoute
import pt.dourobats.app.features.schedule.ScheduleRoute
import pt.dourobats.app.features.settings.SettingsRoute

internal data class BottomNavItem(
    val route: Any,
    val titleRes: StringResource,
    val icon: ImageVector,
)

internal val bottomNavItems = listOf(
    BottomNavItem(HomeRoute, Res.string.nav_home, Icons.Default.Home),
    BottomNavItem(ScheduleRoute, Res.string.nav_training, Icons.Default.CalendarMonth),
    BottomNavItem(SettingsRoute, Res.string.nav_settings, Icons.Default.Settings),
)
