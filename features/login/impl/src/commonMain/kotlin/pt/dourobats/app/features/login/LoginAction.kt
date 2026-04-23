package pt.dourobats.app.features.login

import pt.dourobats.app.features.login.api.LoginMethod

sealed interface LoginAction {
    data object ClearError : LoginAction
    data class UpdateEmail(val email: String) : LoginAction
    data class UpdatePassword(val password: String) : LoginAction
    data object TogglePasswordVisibility : LoginAction
    data object LoginWithEmail : LoginAction
    data class LoginWithSocial(val method: LoginMethod) : LoginAction
}
