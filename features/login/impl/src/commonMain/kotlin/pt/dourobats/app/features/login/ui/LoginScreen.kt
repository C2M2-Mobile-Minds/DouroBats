package pt.dourobats.app.features.login.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dourobats.features.login.generated.resources.Res
import dourobats.features.login.generated.resources.login_app_name
import dourobats.features.login.generated.resources.login_app_tagline
import org.jetbrains.compose.resources.stringResource
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
        LanguageSelectorRow(
            currentLanguage = uiState.currentLanguage,
            onLanguageSelected = { onAction(LoginAction.SelectLanguage(it)) },
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(Res.string.login_app_name),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = stringResource(Res.string.login_app_tagline),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
