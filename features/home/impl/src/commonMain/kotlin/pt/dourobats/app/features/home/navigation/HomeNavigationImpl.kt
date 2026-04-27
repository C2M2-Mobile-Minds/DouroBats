package pt.dourobats.app.features.home.navigation

import pt.dourobats.app.core.navigation.NavigationEvent
import pt.dourobats.app.core.navigation.NavigationManager
import pt.dourobats.app.features.home.HomeNavigation
import pt.dourobats.app.features.home.NewsDetailRoute
import pt.dourobats.app.features.management.ManagementHubRoute

internal class HomeNavigationImpl(
    private val navigationManager: NavigationManager,
) : HomeNavigation {

    override fun navigateToManagementHub() {
        navigationManager.navigate(NavigationEvent.Navigate(ManagementHubRoute))
    }

    override fun navigateToViewReports() {
        // TODO: Navigate to ViewReports screen (admin feature — pending)
    }

    override fun navigateToManageMembers() {
        // TODO: Navigate to ManageMembers screen (admin feature — pending)
    }

    override fun navigateToNewsDetail(newsId: String) {
        navigationManager.navigate(NavigationEvent.Navigate(NewsDetailRoute(newsId)))
    }
}
