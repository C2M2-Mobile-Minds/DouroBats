package pt.dourobats.app.features.login.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dourobats.features.login.generated.resources.Res
import dourobats.features.login.generated.resources.login_change_email_button
import dourobats.features.login.generated.resources.login_verify_button
import dourobats.features.login.generated.resources.login_verify_prompt
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.components.actions.DouroLoginButton
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.login.ui.LoginAction
import pt.dourobats.app.features.login.ui.LoginUiState
import pt.dourobats.app.features.login.ui.OtpInputField

@Composable
internal fun VerifyStep(uiState: LoginUiState, onAction: (LoginAction) -> Unit) {
    val spacing = LocalSpacing.current

    Column(verticalArrangement = Arrangement.spacedBy(spacing.medium)) {
        // Prompt + email address — white text against the gradient
        Text(
            text = stringResource(Res.string.login_verify_prompt),
            style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 0.5.sp),
            fontWeight = FontWeight.ExtraBold,
            color = Color.White.copy(alpha = 0.80f),
        )
        Text(
            text = uiState.email,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )

        Spacer(modifier = Modifier.height(spacing.small))

        // Glass OTP boxes
        OtpInputField(
            code = uiState.code,
            onCodeChanged = { onAction(LoginAction.UpdateCode(it)) },
            isError = uiState.codeError != null,
        )

        uiState.codeError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 4.dp),
            )
        }

        Spacer(modifier = Modifier.height(spacing.small))

        DouroLoginButton(
            text = stringResource(Res.string.login_verify_button),
            onClick = { onAction(LoginAction.SubmitCode) },
            enabled = uiState.isCodeValid && !uiState.isLoading,
            modifier = Modifier.fillMaxWidth(),
        )

        TextButton(
            onClick = { onAction(LoginAction.BackToEmail) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(Res.string.login_change_email_button),
                color = Color.White.copy(alpha = 0.75f),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VerifyStepPreview() {
    AppTheme {
        VerifyStep(
            uiState = LoginUiState(
                email = "athlete@dourobats.pt",
                step = LoginUiState.LoginStep.VERIFY_CODE,
            ),
            onAction = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun VerifyStepErrorPreview() {
    AppTheme {
        VerifyStep(
            uiState = LoginUiState(
                email = "athlete@dourobats.pt",
                code = "123456",
                step = LoginUiState.LoginStep.VERIFY_CODE,
                codeError = "Invalid code. Please try again.",
            ),
            onAction = {},
        )
    }
}
