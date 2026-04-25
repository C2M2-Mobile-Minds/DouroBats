package pt.dourobats.app.features.login.ui

import pt.dourobats.app.core.localization.Language

sealed interface LoginAction {
    data class UpdateEmail(val email: String) : LoginAction
    data class UpdateCode(val code: String) : LoginAction
    data class SelectLanguage(val language: Language) : LoginAction
    data object SubmitEmail : LoginAction
    data object SubmitCode : LoginAction
    data object BackToEmail : LoginAction
}
