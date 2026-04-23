package pt.dourobats.app.features.login.api.exception

sealed class AuthException(message: String) : Exception(message) {
    class InvalidCredentials : AuthException("Invalid email or password")
    class AccountLocked : AuthException("Account has been locked. Please contact support.")
    class TooManyAttempts : AuthException("Too many login attempts. Please try again later.")
    class SessionExpired : AuthException("Your session has expired. Please log in again.")
    class Unknown(message: String = "Authentication failed") : AuthException(message)
}
