package pt.dourobats.app.features.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dourobats.features.settings.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pt.dourobats.app.core.domain.model.Theme
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.settings.components.LanguageBottomSheet
import pt.dourobats.app.features.settings.components.ProfileEditDialog
import pt.dourobats.app.features.settings.components.ProfileHeader
import pt.dourobats.app.features.settings.components.SettingsCard
import pt.dourobats.app.features.settings.components.SettingsSwitchItem

/**
 * Modern card-based Settings screen with Material Design 3 patterns.
 *
 * Features:
 * - Card-based layout for visual grouping
 * - Toggle switch for theme (instant feedback)
 * - Bottom sheet for language selection (better mobile UX)
 * - Responsive spacing using design tokens
 *
 * @param modifier Optional modifier for the screen
 * @param viewModel ViewModel managing settings state and logic
 */
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val spacing = LocalSpacing.current
    val uiState by viewModel.uiState.collectAsState()
    val editState by viewModel.editState.collectAsState()
    var showLanguageSheet by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = spacing.screenHorizontal)
    ) {
        Spacer(modifier = Modifier.height(spacing.standard))

        // Profile Card
        SettingsCard(title = stringResource(Res.string.settings_section_account)) {
            ProfileHeader(
                userProfile = uiState.userProfile,
                onEditClick = { showEditDialog = true }
            )

            Spacer(modifier = Modifier.height(spacing.small))

            // Account details
            AccountDetailItem(
                icon = "✉️",
                label = stringResource(Res.string.settings_email),
                value = uiState.userProfile.email.ifEmpty { stringResource(Res.string.settings_not_set) }
            )

            if (uiState.userProfile.phoneNumber.isNotEmpty()) {
                Spacer(modifier = Modifier.height(spacing.small))
                AccountDetailItem(
                    icon = "\uD83D\uDCF1",
                    label = stringResource(Res.string.settings_phone),
                    value = uiState.userProfile.phoneNumber
                )
            }
        }

        Spacer(modifier = Modifier.height(spacing.sectionSpacing))

        // Preferences Card
        SettingsCard(title = stringResource(Res.string.settings_section_preferences)) {
            // Language selector
            SettingsClickableItem(
                icon = "\uD83C\uDF10",
                title = stringResource(Res.string.settings_language),
                subtitle = uiState.currentLanguage.displayName,
                onClick = { showLanguageSheet = true }
            )

            Spacer(modifier = Modifier.height(spacing.standard))

            // Dark mode toggle
            SettingsSwitchItem(
                icon = "\uD83C\uDFA8",
                title = stringResource(Res.string.settings_theme),
                subtitle = uiState.currentTheme.displayName,
                checked = uiState.currentTheme == Theme.DARK,
                onCheckedChange = { isDark ->
                    viewModel.setTheme(if (isDark) Theme.DARK else Theme.LIGHT)
                }
            )
        }

        Spacer(modifier = Modifier.height(spacing.sectionSpacing))

        // Account Actions Card
        SettingsCard(title = stringResource(Res.string.settings_section_actions)) {
            ActionItem(
                icon = "🚪",
                title = stringResource(Res.string.settings_logout),
                onClick = { viewModel.logout() }
            )

            Spacer(modifier = Modifier.height(spacing.standard))

            ActionItem(
                icon = "🗑️",
                title = stringResource(Res.string.settings_delete_account),
                onClick = { showDeleteDialog = true },
                isDestructive = true
            )
        }

        Spacer(modifier = Modifier.height(spacing.sectionSpacing))
    }

    // Language Bottom Sheet
    if (showLanguageSheet) {
        LanguageBottomSheet(
            currentLanguage = uiState.currentLanguage,
            onLanguageSelected = { language ->
                viewModel.setLanguage(language)
                showLanguageSheet = false
            },
            onDismiss = { showLanguageSheet = false }
        )
    }

    // Profile Edit Dialog
    LaunchedEffect(showEditDialog) {
        if (showEditDialog) {
            viewModel.enterEditMode()
        }
    }

    if (showEditDialog) {
        ProfileEditDialog(
            editState = editState,
            email = uiState.userProfile.email,
            validationErrors = uiState.validationErrors,
            onDisplayNameChange = viewModel::updateDisplayName,
            onPhoneNumberChange = viewModel::updatePhoneNumber,
            onSave = {
                viewModel.saveProfile()
                showEditDialog = false
            },
            onDismiss = {
                viewModel.cancelEdit()
                showEditDialog = false
            }
        )
    }

    // Delete Account Confirmation
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(Res.string.settings_delete_account)) },
            text = { Text(stringResource(Res.string.settings_delete_account_confirm)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteAccount()
                        showDeleteDialog = false
                    }
                ) {
                    Text(
                        stringResource(Res.string.settings_delete),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(Res.string.settings_cancel))
                }
            }
        )
    }
}

/**
 * Account detail display item (read-only).
 */
@Composable
private fun AccountDetailItem(
    icon: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = modifier.padding(vertical = spacing.extraSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.width(spacing.standard))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

/**
 * Clickable settings item (e.g., language selector).
 */
@Composable
private fun SettingsClickableItem(
    icon: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.width(spacing.standard))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Action button item (logout, delete).
 */
@Composable
private fun ActionItem(
    icon: String,
    title: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val textColor = if (isDestructive) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.headlineSmall,
            color = textColor
        )
        Spacer(modifier = Modifier.width(spacing.standard))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor
        )
    }
}
