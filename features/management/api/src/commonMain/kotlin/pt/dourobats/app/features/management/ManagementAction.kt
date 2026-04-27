package pt.dourobats.app.features.management

sealed interface ManagementAction {
    data object CreateSessionRequested : ManagementAction
    data object PostAnnouncementRequested : ManagementAction
    data object Dismiss : ManagementAction
}
