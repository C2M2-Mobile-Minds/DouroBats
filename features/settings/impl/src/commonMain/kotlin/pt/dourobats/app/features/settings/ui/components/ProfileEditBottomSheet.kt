package pt.dourobats.app.features.settings.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dourobats.features.settings.generated.resources.Res
import dourobats.features.settings.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import pt.dourobats.app.core.ui.components.inputs.DouroTextField
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.settings.ui.DisplayNameError
import pt.dourobats.app.features.settings.ui.EmailError
import pt.dourobats.app.features.settings.ui.PhoneError
import pt.dourobats.app.features.settings.ui.ProfileEditState
import pt.dourobats.app.features.settings.ui.SettingsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileEditBottomSheet(
    editState: ProfileEditState,
    validationErrors: SettingsUiState.ValidationErrors,
    onDisplayNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val spacing = LocalSpacing.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = spacing.standard)
                .padding(bottom = spacing.standard)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(Res.string.settings_edit_profile),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = spacing.standard)
            )

            Spacer(modifier = Modifier.height(spacing.small))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = spacing.cardElevation),
            ) {
                Column(modifier = Modifier.padding(spacing.standard)) {
                    DouroTextField(
                        value = editState.displayName,
                        onValueChange = onDisplayNameChange,
                        label = stringResource(Res.string.settings_display_name),
                        error = validationErrors.displayName?.let {
                            when (it) {
                                DisplayNameError.Blank -> stringResource(Res.string.settings_error_display_name_blank)
                                DisplayNameError.TooShort -> stringResource(Res.string.settings_error_display_name_too_short)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(spacing.standard))

                    DouroTextField(
                        value = editState.email,
                        onValueChange = onEmailChange,
                        label = stringResource(Res.string.settings_email),
                        error = validationErrors.email?.let {
                            when (it) {
                                EmailError.Blank -> stringResource(Res.string.settings_error_email_blank)
                                EmailError.InvalidFormat -> stringResource(Res.string.settings_error_email_invalid)
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(spacing.standard))

                    DouroTextField(
                        value = editState.phoneNumber,
                        onValueChange = onPhoneNumberChange,
                        label = stringResource(Res.string.settings_phone),
                        error = validationErrors.phoneNumber?.let {
                            when (it) {
                                PhoneError.Blank -> stringResource(Res.string.settings_error_phone_blank)
                                PhoneError.InvalidFormat -> stringResource(Res.string.settings_error_phone_invalid)
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                }
            }

            Spacer(modifier = Modifier.height(spacing.extraLarge))

            Button(
                onClick = onSave,
                enabled = !validationErrors.hasErrors,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(6.dp),
            ) {
                Text(
                    text = stringResource(Res.string.settings_save_changes),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

