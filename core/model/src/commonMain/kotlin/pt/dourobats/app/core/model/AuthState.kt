package pt.dourobats.app.core.model

/**
 * Represents the authentication state of the user.
 */
sealed class AuthState {
    /**
     * Initial loading state while checking authentication from DataStore.
     * Prevents flickering by keeping the native splash screen visible.
     */
    data object Loading : AuthState()

    /**
     * User is not authenticated.
     */
    data object Unauthenticated : AuthState()

    /**
     * User is authenticated.
     *
     * @property userId Unique identifier for the user
     * @property email User's email address
     * @property loginMethod Method used to log in
     */
    data class Authenticated(
        val userId: String,
        val email: String,
        val loginMethod: LoginMethod
    ) : AuthState()
}

/**
 * Methods available for user authentication.
 */
enum class LoginMethod {
    EMAIL,
    GOOGLE,
    FACEBOOK,
    APPLE
}
