package pt.dourobats.app.features.login.ui

internal class LoginFormValidator {

    fun validateEmail(email: String): String? {
        val trimmed = email.trim()
        return when {
            trimmed.isBlank() -> null
            !trimmed.matches(EMAIL_REGEX) -> "Invalid email format"
            else -> null
        }
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
