package pt.dourobats.app.features.login.api.model

sealed class AuthState {
    object Loading : AuthState()
    object Unauthenticated : AuthState()
    data class Authenticated(val userId: String, val email: String) : AuthState()
}
