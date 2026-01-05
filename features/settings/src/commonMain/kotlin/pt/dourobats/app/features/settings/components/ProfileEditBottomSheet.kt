package pt.dourobats.app.features.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import dourobats.features.settings.generated.resources.Res
import dourobats.features.settings.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.settings.ProfileEditState
import pt.dourobats.app.features.settings.SettingsUiState

/**
 * Modal Bottom Sheet for editing user profile.
 * Provides a modern, mobile-friendly UI for profile editing.
 *
 * @param editState Current state of the profile being edited
 * @param validationErrors Current validation errors for the form
 * @param onDisplayNameChange Callback when display name changes
 * @param onEmailChange Callback when email changes
 * @param onPhoneNumberChange Callback when phone number changes
 * @param onSave Callback to save the profile changes
 * @param onDismiss Callback to dismiss the bottom sheet
 * @param modifier Optional modifier for the bottom sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditBottomSheet(
    editState: ProfileEditState,
    validationErrors: SettingsUiState.ValidationErrors,
    onDisplayNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    val spacing = LocalSpacing.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.standard)
                .verticalScroll(rememberScrollState())
        ) {
            // Title
            Text(
                text = stringResource(Res.string.settings_edit_profile),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(spacing.standard))

            // Display Name field
            OutlinedTextField(
                value = editState.displayName,
                onValueChange = onDisplayNameChange,
                label = { Text(stringResource(Res.string.settings_display_name)) },
                isError = validationErrors.displayName != null,
                supportingText = validationErrors.displayName?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(spacing.standard))

            // Email field
            OutlinedTextField(
                value = editState.email,
                onValueChange = onEmailChange,
                label = { Text(stringResource(Res.string.settings_email)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = validationErrors.email != null,
                supportingText = validationErrors.email?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(spacing.standard))

            // Phone Number field
            OutlinedTextField(
                value = editState.phoneNumber,
                onValueChange = onPhoneNumberChange,
                label = { Text(stringResource(Res.string.settings_phone)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = validationErrors.phoneNumber != null,
                supportingText = validationErrors.phoneNumber?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(spacing.large))

            // Save button
            Button(
                onClick = onSave,
                enabled = !validationErrors.hasErrors,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(Res.string.settings_save_changes))
            }

            // Bottom padding for safe area
            Spacer(modifier = Modifier.height(spacing.standard))
        }
    }
}
