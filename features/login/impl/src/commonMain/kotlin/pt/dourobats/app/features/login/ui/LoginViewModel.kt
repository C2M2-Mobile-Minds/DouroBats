package pt.dourobats.app.features.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.login.api.usecase.RequestLoginCodeUseCase
import pt.dourobats.app.features.login.api.usecase.VerifyLoginCodeUseCase
import pt.dourobats.app.features.login.ui.LoginUiState.LoginStep
import pt.dourobats.app.features.settings.api.usecase.ObserveLanguageUseCase
import pt.dourobats.app.features.settings.api.usecase.SetLanguageUseCase

internal class LoginViewModel(
    private val requestLoginCode: RequestLoginCodeUseCase,
    private val verifyLoginCode: VerifyLoginCodeUseCase,
    private val validator: LoginFormValidator,
    private val errorMapper: LoginErrorMapper,
    private val observeLanguage: ObserveLanguageUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeLanguage().collect { language ->
                _uiState.update { it.copy(currentLanguage = language) }
            }
        }
    }

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.UpdateEmail -> {
                val error = validator.validateEmail(action.email)
                _uiState.update { it.copy(email = action.email, emailError = error, errorMessage = null) }
            }
            is LoginAction.UpdateCode -> {
                val error = if (action.code.length == 6 || action.code.isEmpty()) null else "Enter 6 digits"
                _uiState.update { it.copy(code = action.code, codeError = error, errorMessage = null) }
            }
            is LoginAction.SelectLanguage -> viewModelScope.launch { setLanguageUseCase(action.language) }
            LoginAction.SubmitEmail -> requestEmailCode()
            LoginAction.SubmitCode -> verifyOtpCode()
            LoginAction.BackToEmail -> _uiState.update {
                it.copy(step = LoginStep.EMAIL, code = "", codeError = null, errorMessage = null)
            }
        }
    }

    private fun requestEmailCode() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = requestLoginCode(_uiState.value.email)) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false, step = LoginStep.VERIFY_CODE) }
                is Result.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = errorMapper.mapToUserMessage(result.exception))
                }
                is Result.Loading -> Unit
            }
        }
    }

    private fun verifyOtpCode() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = verifyLoginCode(_uiState.value.email, _uiState.value.code)
            if (result is Result.Error) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = errorMapper.mapToUserMessage(result.exception))
                }
            }
        }
    }
}
