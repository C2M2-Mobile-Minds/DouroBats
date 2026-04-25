package pt.dourobats.app.core.navigation

import pt.dourobats.app.core.common.navigation.AppRoute

sealed class NavigationEvent {
    data class Navigate(val route: AppRoute) : NavigationEvent()
    data object NavigateBack : NavigationEvent()
}
