package pt.dourobats.app.features.login.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun VerifyStep(uiState: LoginUiState, onAction: (LoginAction) -> Unit) {
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
        OtpInputField(
            code = uiState.code,
            onCodeChanged = { onAction(LoginAction.UpdateCode(it)) },
            isError = uiState.codeError != null,
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
