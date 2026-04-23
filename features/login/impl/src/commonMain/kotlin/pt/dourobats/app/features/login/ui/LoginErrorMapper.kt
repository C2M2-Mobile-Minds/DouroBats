package pt.dourobats.app.features.login.ui

import pt.dourobats.app.features.login.api.exception.AuthException
import pt.dourobats.app.core.common.exception.NetworkException
import pt.dourobats.app.core.common.exception.ValidationException

internal class LoginErrorMapper {
    fun mapToUserMessage(error: Throwable): String = when (error) {
        is ValidationException.RequiredField -> error.message ?: "This field is required"
        is ValidationException.Generic -> error.message ?: "Please check your input"
        is AuthException.InvalidCredentials -> "Incorrect code. Please try again."
        is AuthException.TooManyAttempts -> "Too many attempts. Please request a new code."
        is AuthException.SessionExpired -> "Your session has expired. Please log in again."
        is AuthException.AccountLocked -> "Your account has been locked. Please contact support."
        is AuthException.Unknown -> "Something went wrong. Please try again."
        is NetworkException -> "Network error. Please check your connection."
        else -> "Something went wrong. Please try again."
    }
}
