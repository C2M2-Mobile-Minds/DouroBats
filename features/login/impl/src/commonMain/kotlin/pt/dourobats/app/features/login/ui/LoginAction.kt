package pt.dourobats.app.features.login.ui

sealed interface LoginAction {
    data class UpdateEmail(val email: String) : LoginAction
    data class UpdateCode(val code: String) : LoginAction
    data object SubmitEmail : LoginAction
    data object SubmitCode : LoginAction
    data object BackToEmail : LoginAction
}
