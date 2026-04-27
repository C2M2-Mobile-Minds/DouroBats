package pt.dourobats.app.features.home.ui

internal sealed interface HomeAction {
    data object OnManageFabClick : HomeAction
    data object OnViewReportsClick : HomeAction
    data object OnManageMembersClick : HomeAction
    data class OnNewsClick(val newsId: String) : HomeAction
}
