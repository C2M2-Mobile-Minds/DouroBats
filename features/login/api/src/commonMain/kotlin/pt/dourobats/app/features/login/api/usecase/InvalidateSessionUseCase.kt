package pt.dourobats.app.features.login.api.usecase

/**
 * Invalidates the current session without a user-initiated logout.
 * Call this from any feature when a network response returns 401/403
 * or when [pt.dourobats.app.features.login.api.exception.AuthException.SessionExpired] is caught.
 *
 * Clears the stored auth state, causing [ObserveAuthStateUseCase] to emit
 * [pt.dourobats.app.features.login.api.model.AuthState.Unauthenticated] and
 * the app to redirect to the login screen automatically.
 */
interface InvalidateSessionUseCase {
    suspend operator fun invoke()
}
