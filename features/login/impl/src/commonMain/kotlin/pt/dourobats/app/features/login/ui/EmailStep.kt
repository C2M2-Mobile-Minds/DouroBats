package pt.dourobats.app.features.login.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dourobats.features.login.generated.resources.Res
import dourobats.features.login.generated.resources.login_email_label
import dourobats.features.login.generated.resources.login_email_placeholder
import dourobats.features.login.generated.resources.login_email_prompt
import dourobats.features.login.generated.resources.login_send_code_button
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun EmailStep(uiState: LoginUiState, onAction: (LoginAction) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = stringResource(Res.string.login_email_prompt))
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { onAction(LoginAction.UpdateEmail(it)) },
            label = { Text(stringResource(Res.string.login_email_label)) },
            placeholder = { Text(stringResource(Res.string.login_email_placeholder)) },
            isError = uiState.emailError != null,
            supportingText = uiState.emailError?.let { { Text(it) } },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = {
                if (uiState.isEmailValid) onAction(LoginAction.SubmitEmail)
            }),
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
                Text(stringResource(Res.string.login_send_code_button))
            }
        }
    }
}
