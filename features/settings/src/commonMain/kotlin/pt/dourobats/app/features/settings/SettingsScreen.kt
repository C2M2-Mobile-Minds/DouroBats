package pt.dourobats.app.features.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.graphics.Color
import dourobats.features.settings.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.settings.components.ProfileEditDialog
import pt.dourobats.app.features.settings.components.ProfileHeader
import pt.dourobats.app.features.settings.components.ThemeSelectionDialog

/**
 * Settings screen showing user profile, preferences, and account actions.
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
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.screenHorizontal),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.settings_title),
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(spacing.extraSmall))
            Text(
                text = stringResource(Res.string.settings_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Divider()

        // Settings List
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Profile Header
            item {
                ProfileHeader(
                    userProfile = uiState.userProfile,
                    onEditClick = { showEditDialog = true }
                )
                Divider()
            }

            // Account Section
            item {
                SectionHeader(title = stringResource(Res.string.settings_section_account))
            }

            item {
                SettingsItem(
                    icon = "\uD83D\uDCBC",
                    title = stringResource(Res.string.settings_display_name),
                    subtitle = uiState.userProfile.displayName.ifEmpty { stringResource(Res.string.settings_not_set) },
                    onClick = { showEditDialog = true }
                )
            }

            item {
                SettingsItem(
                    icon = "✉️",
                    title = stringResource(Res.string.settings_email),
                    subtitle = uiState.userProfile.email.ifEmpty { stringResource(Res.string.settings_not_set) },
                    onClick = { } // Read-only
                )
            }

            item {
                SettingsItem(
                    icon = "\uD83D\uDCF1",
                    title = stringResource(Res.string.settings_phone),
                    subtitle = uiState.userProfile.phoneNumber.ifEmpty { stringResource(Res.string.settings_not_set) },
                    onClick = { showEditDialog = true }
                )
            }

            item {
                Divider()
            }

            // Preferences Section
            item {
                SectionHeader(title = stringResource(Res.string.settings_section_preferences))
            }

            item {
                SettingsItem(
                    icon = "\uD83C\uDF10",
                    title = stringResource(Res.string.settings_language),
                    subtitle = uiState.currentLanguage.displayName,
                    onClick = { showLanguageDialog = true }
                )
            }

            item {
                SettingsItem(
                    icon = "\uD83C\uDFA8",
                    title = stringResource(Res.string.settings_theme),
                    subtitle = uiState.currentTheme.displayName,
                    onClick = { showThemeDialog = true }
                )
            }

            item {
                Divider()
            }

            // Actions Section
            item {
                SectionHeader(title = stringResource(Res.string.settings_section_actions))
            }

            item {
                ActionItem(
                    icon = "🚪",
                    title = stringResource(Res.string.settings_logout),
                    onClick = { viewModel.logout() }
                )
            }

            item {
                ActionItem(
                    icon = "🗑️",
                    title = stringResource(Res.string.settings_delete_account),
                    onClick = { showDeleteDialog = true },
                    textColor = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    // Dialogs
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = uiState.currentLanguage,
            onLanguageSelected = { language ->
                viewModel.setLanguage(language)
                showLanguageDialog = false
            },
            onDismiss = { showLanguageDialog = false }
        )
    }

    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = uiState.currentTheme,
            onThemeSelected = { theme ->
                viewModel.setTheme(theme)
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }

    // Only call enterEditMode when dialog is first shown
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
                    Text(stringResource(Res.string.settings_delete), color = MaterialTheme.colorScheme.error)
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
 * Section header composable for grouping settings.
 *
 * @param title Section title
 * @param modifier Optional modifier
 */
@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(horizontal = spacing.screenHorizontal, vertical = spacing.small)
    )
}

/**
 * Settings item with icon, title, and subtitle.
 *
 * @param icon Emoji icon
 * @param title Item title
 * @param subtitle Item subtitle/value
 * @param onClick Click callback
 * @param modifier Optional modifier
 */
@Composable
private fun SettingsItem(
    icon: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(subtitle) },
        leadingContent = {
            Text(
                text = icon,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        modifier = modifier.clickable(onClick = onClick)
    )
}

/**
 * Action item with icon and title (for logout, delete account, etc.).
 *
 * @param icon Emoji icon
 * @param title Action title
 * @param onClick Click callback
 * @param modifier Optional modifier
 * @param textColor Text color (default or error)
 */
@Composable
private fun ActionItem(
    icon: String,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    ListItem(
        headlineContent = { Text(title, color = textColor) },
        leadingContent = {
            Text(
                text = icon,
                style = MaterialTheme.typography.headlineSmall,
                color = textColor
            )
        },
        modifier = modifier.clickable(onClick = onClick)
    )
}
