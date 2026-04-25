package pt.dourobats.app.features.login.ui

import pt.dourobats.app.core.localization.Language

internal data class LoginUiState(
    val email: String = "",
    val code: String = "",
    val step: LoginStep = LoginStep.EMAIL,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val emailError: String? = null,
    val codeError: String? = null,
    val currentLanguage: Language = Language.ENGLISH_US,
) {
    enum class LoginStep { EMAIL, VERIFY_CODE }

    val isEmailValid: Boolean get() = email.isNotBlank() && emailError == null
    val isCodeValid: Boolean get() = code.length == 6 && codeError == null
}
