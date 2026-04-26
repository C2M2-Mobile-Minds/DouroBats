package pt.dourobats.app.features.login.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import dourobats.features.login.generated.resources.Res
import dourobats.features.login.generated.resources.login_email_label
import dourobats.features.login.generated.resources.login_email_placeholder
import dourobats.features.login.generated.resources.login_email_prompt
import dourobats.features.login.generated.resources.login_send_code_button
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.localization.Language
import pt.dourobats.app.core.ui.components.actions.DouroLoginButton
import pt.dourobats.app.core.ui.components.inputs.DouroLoginTextField
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.login.ui.LoginAction
import pt.dourobats.app.features.login.ui.LoginUiState

@Composable
internal fun EmailStep(uiState: LoginUiState, onAction: (LoginAction) -> Unit) {
    val spacing = LocalSpacing.current

    Column(verticalArrangement = Arrangement.spacedBy(spacing.medium)) {
        Text(
            text = stringResource(Res.string.login_email_prompt),
            style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 0.5.sp),
            fontWeight = FontWeight.ExtraBold,
            color = Color.White.copy(alpha = 0.85f),
        )

        DouroLoginTextField(
            value = uiState.email,
            onValueChange = { onAction(LoginAction.UpdateEmail(it)) },
            label = stringResource(Res.string.login_email_label),
            isError = uiState.emailError != null,
            supportingText = uiState.emailError?.let {
                { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(spacing.small))

        DouroLoginButton(
            text = stringResource(Res.string.login_send_code_button),
            onClick = { onAction(LoginAction.SubmitEmail) },
            enabled = uiState.isEmailValid && !uiState.isLoading,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmailStepPreview() {
    AppTheme {
        EmailStep(
            uiState = LoginUiState(email = "athlete@dourobats.pt"),
            onAction = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmailStepErrorPreview() {
    AppTheme {
        EmailStep(
            uiState = LoginUiState(email = "bad-email", emailError = "Invalid email address"),
            onAction = {},
        )
    }
}
