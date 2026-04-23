package pt.dourobats.app.features.login.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import pt.dourobats.app.features.login.ui.LoginUiState.LoginStep

@Composable
fun LoginRoute() {
    val viewModel: LoginViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LoginScreen(uiState = uiState, onAction = viewModel::onAction)
}

@Composable
internal fun LoginScreen(
    uiState: LoginUiState,
    onAction: (LoginAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "DouroBats",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "Douro Bat Polo Club",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(48.dp))

        AnimatedContent(targetState = uiState.step) { step ->
            when (step) {
                LoginStep.EMAIL -> EmailInputSection(uiState = uiState, onAction = onAction)
                LoginStep.VERIFY_CODE -> CodeInputSection(uiState = uiState, onAction = onAction)
            }
        }

        uiState.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun EmailInputSection(uiState: LoginUiState, onAction: (LoginAction) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Enter your email to receive a login code.",
            style = MaterialTheme.typography.bodyLarge,
        )
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { onAction(LoginAction.UpdateEmail(it)) },
            label = { Text("Email address") },
            isError = uiState.emailError != null,
            supportingText = uiState.emailError?.let { { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Button(
            onClick = { onAction(LoginAction.SubmitEmail) },
            enabled = uiState.isEmailValid && !uiState.isLoading,
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
            } else {
                Text("Send Code")
            }
        }
    }
}

@Composable
private fun CodeInputSection(uiState: LoginUiState, onAction: (LoginAction) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Enter the 6-digit code sent to",
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = uiState.email,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )
        OutlinedTextField(
            value = uiState.code,
            onValueChange = { if (it.length <= 6) onAction(LoginAction.UpdateCode(it)) },
            label = { Text("6-digit code") },
            isError = uiState.codeError != null,
            supportingText = uiState.codeError?.let { { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Button(
            onClick = { onAction(LoginAction.SubmitCode) },
            enabled = uiState.isCodeValid && !uiState.isLoading,
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
            } else {
                Text("Verify & Login")
            }
        }
        TextButton(
            onClick = { onAction(LoginAction.BackToEmail) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Change Email")
        }
    }
}
