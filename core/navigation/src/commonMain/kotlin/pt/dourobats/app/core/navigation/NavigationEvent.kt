package pt.dourobats.app.core.navigation

sealed class NavigationEvent {
    data class Navigate(val route: Any) : NavigationEvent()
    data object NavigateBack : NavigationEvent()
}
