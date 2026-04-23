package pt.dourobats.app.features.home.ui

sealed interface HomeAction {
    data object OnCreateSessionClick : HomeAction
    data object OnViewReportsClick : HomeAction
    data object OnManageMembersClick : HomeAction
    data class OnNewsClick(val newsId: String) : HomeAction
}
