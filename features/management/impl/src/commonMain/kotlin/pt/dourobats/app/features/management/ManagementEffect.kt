package pt.dourobats.app.features.management

internal sealed interface ManagementEffect {
    data object NavigateToCreateSession : ManagementEffect
    data object NavigateToAnnouncement : ManagementEffect
    data object NavigateToCheckIn : ManagementEffect
    data object Back : ManagementEffect
}
