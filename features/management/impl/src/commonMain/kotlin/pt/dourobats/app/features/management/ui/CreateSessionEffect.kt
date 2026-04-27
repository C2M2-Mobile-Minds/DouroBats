package pt.dourobats.app.features.management.ui

internal sealed interface CreateSessionEffect {
    data class SessionCreated(val sessionId: String, val sessionName: String) : CreateSessionEffect
    data object NavigateBack : CreateSessionEffect
}
