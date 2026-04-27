package pt.dourobats.app.features.home

interface HomeNavigation {
    fun navigateToManagementHub()
    fun navigateToViewReports()
    fun navigateToManageMembers()
    fun navigateToNewsDetail(newsId: String)
}
