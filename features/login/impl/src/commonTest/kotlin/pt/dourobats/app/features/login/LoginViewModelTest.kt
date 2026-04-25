package pt.dourobats.app.features.login

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.flow.MutableStateFlow
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.core.localization.Language
import pt.dourobats.app.features.login.api.exception.AuthException
import pt.dourobats.app.features.login.testing.FakeRequestLoginCodeUseCase
import pt.dourobats.app.features.login.testing.FakeVerifyLoginCodeUseCase
import pt.dourobats.app.features.login.ui.LoginAction
import pt.dourobats.app.features.login.ui.LoginErrorMapper
import pt.dourobats.app.features.login.ui.LoginFormValidator
import pt.dourobats.app.features.login.ui.LoginUiState.LoginStep
import pt.dourobats.app.features.login.ui.LoginViewModel
import pt.dourobats.app.features.settings.api.usecase.ObserveLanguageUseCase
import pt.dourobats.app.features.settings.api.usecase.SetLanguageUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var fakeRequestLoginCode: FakeRequestLoginCodeUseCase
    private lateinit var fakeVerifyLoginCode: FakeVerifyLoginCodeUseCase
    private val testDispatcher = StandardTestDispatcher()

    private val noOpObserveLanguage = object : ObserveLanguageUseCase {
        override fun invoke() = MutableStateFlow(Language.ENGLISH_US)
    }
    private val noOpSetLanguage = object : SetLanguageUseCase {
        override suspend fun invoke(language: Language) = Unit
    }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRequestLoginCode = FakeRequestLoginCodeUseCase().apply {
            invoke = { _ -> Result.Success(Unit) }
        }
        fakeVerifyLoginCode = FakeVerifyLoginCodeUseCase().apply {
            invoke = { _, _ -> Result.Success(Unit) }
        }
        viewModel = LoginViewModel(
            requestLoginCode = fakeRequestLoginCode.build(),
            verifyLoginCode = fakeVerifyLoginCode.build(),
            validator = LoginFormValidator(),
            errorMapper = LoginErrorMapper(),
            observeLanguage = noOpObserveLanguage,
            setLanguageUseCase = noOpSetLanguage,
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is EMAIL step`() {
        val state = viewModel.uiState.value
        assertEquals(LoginStep.EMAIL, state.step)
        assertEquals("", state.email)
        assertEquals("", state.code)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
    }

    @Test
    fun `UpdateEmail updates email field`() {
        viewModel.onAction(LoginAction.UpdateEmail("test@example.com"))
        assertEquals("test@example.com", viewModel.uiState.value.email)
    }

    @Test
    fun `UpdateEmail with invalid format shows error`() {
        viewModel.onAction(LoginAction.UpdateEmail("invalid-email"))
        assertEquals("Invalid email format", viewModel.uiState.value.emailError)
    }

    @Test
    fun `UpdateEmail with valid format clears emailError`() {
        viewModel.onAction(LoginAction.UpdateEmail("test@example.com"))
        assertNull(viewModel.uiState.value.emailError)
    }

    @Test
    fun `UpdateEmail clears general error message`() = runTest(testDispatcher) {
        fakeRequestLoginCode = FakeRequestLoginCodeUseCase().apply {
            invoke = { _ -> Result.Error(AuthException.Unknown()) }
        }
        viewModel = LoginViewModel(
            requestLoginCode = fakeRequestLoginCode.build(),
            verifyLoginCode = fakeVerifyLoginCode.build(),
            validator = LoginFormValidator(),
            errorMapper = LoginErrorMapper(),
            observeLanguage = noOpObserveLanguage,
            setLanguageUseCase = noOpSetLanguage,
        )
        viewModel.onAction(LoginAction.UpdateEmail("test@example.com"))
        viewModel.onAction(LoginAction.SubmitEmail)
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.errorMessage != null)

        viewModel.onAction(LoginAction.UpdateEmail("new@example.com"))
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `SubmitEmail on success transitions to VERIFY_CODE step`() = runTest(testDispatcher) {
        viewModel.onAction(LoginAction.UpdateEmail("test@example.com"))
        viewModel.onAction(LoginAction.SubmitEmail)
        advanceUntilIdle()
        assertEquals(LoginStep.VERIFY_CODE, viewModel.uiState.value.step)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `SubmitEmail shows error on failure`() = runTest(testDispatcher) {
        fakeRequestLoginCode = FakeRequestLoginCodeUseCase().apply {
            invoke = { _ -> Result.Error(AuthException.Unknown()) }
        }
        viewModel = LoginViewModel(
            requestLoginCode = fakeRequestLoginCode.build(),
            verifyLoginCode = fakeVerifyLoginCode.build(),
            validator = LoginFormValidator(),
            errorMapper = LoginErrorMapper(),
            observeLanguage = noOpObserveLanguage,
            setLanguageUseCase = noOpSetLanguage,
        )
        viewModel.onAction(LoginAction.UpdateEmail("test@example.com"))
        viewModel.onAction(LoginAction.SubmitEmail)
        advanceUntilIdle()
        assertEquals(LoginStep.EMAIL, viewModel.uiState.value.step)
        assertTrue(viewModel.uiState.value.errorMessage != null)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `SubmitEmail sets loading false after request completes`() = runTest(testDispatcher) {
        viewModel.onAction(LoginAction.UpdateEmail("test@example.com"))
        viewModel.onAction(LoginAction.SubmitEmail)
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `UpdateCode updates code field`() {
        viewModel.onAction(LoginAction.UpdateCode("123456"))
        assertEquals("123456", viewModel.uiState.value.code)
    }

    @Test
    fun `UpdateCode with partial code shows error`() {
        viewModel.onAction(LoginAction.UpdateCode("123"))
        assertEquals("Enter 6 digits", viewModel.uiState.value.codeError)
    }

    @Test
    fun `UpdateCode with 6 digits clears codeError`() {
        viewModel.onAction(LoginAction.UpdateCode("123456"))
        assertNull(viewModel.uiState.value.codeError)
    }

    @Test
    fun `SubmitCode calls verifyLoginCode`() = runTest(testDispatcher) {
        var capturedCode: String? = null
        fakeVerifyLoginCode = FakeVerifyLoginCodeUseCase().apply {
            invoke = { _, code -> capturedCode = code; Result.Success(Unit) }
        }
        viewModel = LoginViewModel(
            requestLoginCode = fakeRequestLoginCode.build(),
            verifyLoginCode = fakeVerifyLoginCode.build(),
            validator = LoginFormValidator(),
            errorMapper = LoginErrorMapper(),
            observeLanguage = noOpObserveLanguage,
            setLanguageUseCase = noOpSetLanguage,
        )
        viewModel.onAction(LoginAction.UpdateCode("123456"))
        viewModel.onAction(LoginAction.SubmitCode)
        advanceUntilIdle()
        assertEquals("123456", capturedCode)
    }

    @Test
    fun `SubmitCode shows error on wrong code`() = runTest(testDispatcher) {
        fakeVerifyLoginCode = FakeVerifyLoginCodeUseCase().apply {
            invoke = { _, _ -> Result.Error(AuthException.InvalidCredentials()) }
        }
        viewModel = LoginViewModel(
            requestLoginCode = fakeRequestLoginCode.build(),
            verifyLoginCode = fakeVerifyLoginCode.build(),
            validator = LoginFormValidator(),
            errorMapper = LoginErrorMapper(),
            observeLanguage = noOpObserveLanguage,
            setLanguageUseCase = noOpSetLanguage,
        )
        viewModel.onAction(LoginAction.UpdateCode("000000"))
        viewModel.onAction(LoginAction.SubmitCode)
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.errorMessage != null)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `BackToEmail resets to EMAIL step`() = runTest(testDispatcher) {
        viewModel.onAction(LoginAction.UpdateEmail("test@example.com"))
        viewModel.onAction(LoginAction.SubmitEmail)
        advanceUntilIdle()
        assertEquals(LoginStep.VERIFY_CODE, viewModel.uiState.value.step)

        viewModel.onAction(LoginAction.BackToEmail)
        assertEquals(LoginStep.EMAIL, viewModel.uiState.value.step)
        assertEquals("", viewModel.uiState.value.code)
        assertNull(viewModel.uiState.value.codeError)
        assertNull(viewModel.uiState.value.errorMessage)
    }
}
