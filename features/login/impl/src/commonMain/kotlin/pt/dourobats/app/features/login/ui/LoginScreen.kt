package pt.dourobats.app.features.login.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dourobats.features.login.generated.resources.Res
import dourobats.features.login.generated.resources.login_app_name
import dourobats.features.login.generated.resources.login_app_tagline
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.localization.Language
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.features.login.ui.LoginUiState.LoginStep
import pt.dourobats.app.features.login.ui.components.EmailStep
import pt.dourobats.app.features.login.ui.components.VerifyStep

@Composable
internal fun LoginRoute() {
    val viewModel: LoginViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LoginScreen(uiState = uiState, onAction = viewModel::onAction)
}

@Composable
internal fun LoginScreen(
    uiState: LoginUiState,
    onAction: (LoginAction) -> Unit,
) {
    val loginGradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,                           // Midnight Pitch #001E40
            MaterialTheme.colorScheme.primary.copy(alpha = 0.88f),
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.80f), // Power Blue fade
        ),
        start = Offset.Zero,
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = loginGradient)
            .imePadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        LanguageSelectorRow(
            currentLanguage = uiState.currentLanguage,
            onLanguageSelected = { onAction(LoginAction.SelectLanguage(it)) },
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(Res.string.login_app_name).uppercase(),
            style = MaterialTheme.typography.displaySmall.copy(letterSpacing = 2.sp),
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
        )
        Text(
            text = stringResource(Res.string.login_app_tagline),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White.copy(alpha = 0.75f),
        )

        Spacer(modifier = Modifier.height(48.dp))

        AnimatedContent(targetState = uiState.step) { step ->
            when (step) {
                LoginStep.EMAIL -> EmailStep(uiState = uiState, onAction = onAction)
                LoginStep.VERIFY_CODE -> VerifyStep(uiState = uiState, onAction = onAction)
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

@Preview(showBackground = true)
@Composable
private fun LoginScreenEmailStepPreview() {
    AppTheme {
        LoginScreen(
            uiState = LoginUiState(
                email = "",
                step = LoginStep.EMAIL,
                currentLanguage = Language.PORTUGUESE_PT,
            ),
            onAction = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenVerifyStepPreview() {
    AppTheme {
        LoginScreen(
            uiState = LoginUiState(
                email = "athlete@dourobats.pt",
                step = LoginStep.VERIFY_CODE,
                currentLanguage = Language.PORTUGUESE_PT,
            ),
            onAction = {},
        )
    }
}