package pt.dourobats.app.features.home

interface HomeNavigation {
    fun navigateToCreateSession()
    fun navigateToViewReports()
    fun navigateToManageMembers()
    fun navigateToNewsDetail(newsId: String)
}
