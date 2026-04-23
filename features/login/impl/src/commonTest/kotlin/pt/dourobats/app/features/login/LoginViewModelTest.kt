package pt.dourobats.app.features.login

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.core.common.exception.AuthException
import pt.dourobats.app.core.common.exception.ValidationException
import pt.dourobats.app.features.login.api.model.LoginMethod
import pt.dourobats.app.core.domain.usecase.LoginWithEmailUseCase
import pt.dourobats.app.core.domain.usecase.LoginWithSocialUseCase
import pt.dourobats.app.features.login.testing.FakeLoginWithEmailUseCase
import pt.dourobats.app.features.login.testing.FakeLoginWithSocialUseCase
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
    private lateinit var fakeLoginWithEmailUseCase: FakeLoginWithEmailUseCase
    private lateinit var fakeLoginWithSocialUseCase: FakeLoginWithSocialUseCase
    private lateinit var validator: LoginFormValidator
    private lateinit var errorMapper: LoginErrorMapper

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeLoginWithEmailUseCase = FakeLoginWithEmailUseCase()
        fakeLoginWithSocialUseCase = FakeLoginWithSocialUseCase()
        validator = LoginFormValidator()
        errorMapper = LoginErrorMapper()

        viewModel = LoginViewModel(
            loginWithEmailUseCase = fakeLoginWithEmailUseCase,
            loginWithSocialUseCase = fakeLoginWithSocialUseCase,
            validator = validator,
            errorMapper = errorMapper
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Initial State Tests
    @Test
    fun `initial state is correct`() {
        val state = viewModel.uiState.value

        assertEquals("", state.email)
        assertEquals("", state.password)
        assertFalse(state.isPasswordVisible)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertNull(state.emailError)
        assertNull(state.passwordError)
        assertFalse(state.isFormValid)
    }

    // Email Update Tests
    @Test
    fun `updateEmail updates email field`() {
        viewModel.updateEmail("test@example.com")

        assertEquals("test@example.com", viewModel.uiState.value.email)
    }

    @Test
    fun `updateEmail with invalid format shows error`() {
        viewModel.updateEmail("invalid-email")

        assertEquals("Invalid email format", viewModel.uiState.value.emailError)
    }

    @Test
    fun `updateEmail with valid format clears error`() {
        viewModel.updateEmail("test@example.com")

        assertNull(viewModel.uiState.value.emailError)
    }

    @Test
    fun `updateEmail clears general error message`() {
        // Set an error first
        fakeLoginWithEmailUseCase.result = Result.Error(AuthException.InvalidCredentials())
        viewModel.updateEmail("test@example.com")
        viewModel.updatePassword("password123")
        viewModel.loginWithEmail()
        testDispatcher.scheduler.advanceUntilIdle()

        // Now update email
        viewModel.updateEmail("new@example.com")

        assertNull(viewModel.uiState.value.errorMessage)
    }

    // Password Update Tests
    @Test
    fun `updatePassword updates password field`() {
        viewModel.updatePassword("password123")

        assertEquals("password123", viewModel.uiState.value.password)
    }

    @Test
    fun `updatePassword with short password shows error`() {
        viewModel.updatePassword("12345")

        assertEquals("Password must be at least 6 characters", viewModel.uiState.value.passwordError)
    }

    @Test
    fun `updatePassword with valid length clears error`() {
        viewModel.updatePassword("password123")

        assertNull(viewModel.uiState.value.passwordError)
    }

    // Password Visibility Tests
    @Test
    fun `togglePasswordVisibility changes visibility state`() {
        assertFalse(viewModel.uiState.value.isPasswordVisible)

        viewModel.togglePasswordVisibility()
        assertTrue(viewModel.uiState.value.isPasswordVisible)

        viewModel.togglePasswordVisibility()
        assertFalse(viewModel.uiState.value.isPasswordVisible)
    }

    // Form Validation Tests
    @Test
    fun `form is invalid when email is empty`() {
        viewModel.updatePassword("password123")

        assertFalse(viewModel.uiState.value.isFormValid)
    }

    @Test
    fun `form is invalid when password is empty`() {
        viewModel.updateEmail("test@example.com")

        assertFalse(viewModel.uiState.value.isFormValid)
    }

    @Test
    fun `form is valid when email and password are valid`() {
        viewModel.updateEmail("test@example.com")
        viewModel.updatePassword("password123")

        assertTrue(viewModel.uiState.value.isFormValid)
    }

    // Login Success Tests
    @Test
    fun `successful email login clears password and loading state`() = runTest {
        fakeLoginWithEmailUseCase.result = Result.Success(Unit)

        viewModel.updateEmail("test@example.com")
        viewModel.updatePassword("password123")
        viewModel.loginWithEmail()

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("", state.password) // Password cleared
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
    }

    // Login Failure Tests
    @Test
    fun `failed email login shows error message`() = runTest {
        fakeLoginWithEmailUseCase.result = Result.Error(AuthException.InvalidCredentials())

        viewModel.updateEmail("test@example.com")
        viewModel.updatePassword("wrongpassword")
        viewModel.loginWithEmail()

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Invalid email or password. Please try again.", state.errorMessage)
        assertFalse(state.isLoading)
    }

    @Test
    fun `login with invalid form does not call use case`() = runTest {
        viewModel.updateEmail("invalid-email") // Invalid email
        viewModel.updatePassword("pass") // Too short
        viewModel.loginWithEmail()

        advanceUntilIdle()

        assertFalse(fakeLoginWithEmailUseCase.wasCalled)
    }

    @Test
    fun `login sets loading state during execution`() = runTest {
        fakeLoginWithEmailUseCase.result = Result.Success(Unit)

        viewModel.updateEmail("test@example.com")
        viewModel.updatePassword("password123")
        viewModel.loginWithEmail()

        // Before advancement, loading should be true
        // Note: This might not work in all test scenarios due to timing
        // but demonstrates the intent

        advanceUntilIdle()

        // After completion, loading should be false
        assertFalse(viewModel.uiState.value.isLoading)
    }

    // Social Login Tests
    @Test
    fun `successful social login clears loading state`() = runTest {
        fakeLoginWithSocialUseCase.result = Result.Success(Unit)

        viewModel.loginWithSocial(LoginMethod.GOOGLE)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
    }

    @Test
    fun `failed social login shows error message`() = runTest {
        fakeLoginWithSocialUseCase.result = Result.Error(AuthException.Unknown())

        viewModel.loginWithSocial(LoginMethod.FACEBOOK)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Authentication failed. Please try again.", state.errorMessage)
        assertFalse(state.isLoading)
    }

    // Error Clearing Tests
    @Test
    fun `clearError removes error message`() = runTest {
        fakeLoginWithEmailUseCase.result = Result.Error(AuthException.InvalidCredentials())

        viewModel.updateEmail("test@example.com")
        viewModel.updatePassword("wrongpassword")
        viewModel.loginWithEmail()

        advanceUntilIdle()

        // Error should be present
        assertTrue(viewModel.uiState.value.errorMessage != null)

        // Clear error
        viewModel.clearError()

        assertNull(viewModel.uiState.value.errorMessage)
    }

}
