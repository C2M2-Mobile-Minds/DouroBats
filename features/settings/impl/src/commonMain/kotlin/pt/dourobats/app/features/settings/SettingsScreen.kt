package pt.dourobats.app.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dourobats.features.settings.generated.resources.Res
import dourobats.features.settings.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pt.dourobats.app.core.model.Theme
import pt.dourobats.app.core.model.UserRole
import pt.dourobats.app.core.ui.components.AppHeader
import pt.dourobats.app.core.ui.components.SectionHeader
import pt.dourobats.app.core.ui.components.SettingsRowItem
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.core.common.isDebug
import pt.dourobats.app.features.settings.components.DeveloperOptionsBottomSheet
import pt.dourobats.app.features.settings.components.LanguageBottomSheet
import pt.dourobats.app.features.settings.components.NotificationsBottomSheet
import pt.dourobats.app.features.settings.components.ProfileEditBottomSheet

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
    var showNotificationsSheet by remember { mutableStateOf(false) }
    var showDeveloperSheet by remember { mutableStateOf(false) }

    if (!uiState.isDataLoaded) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val userProfile = uiState.userProfile!!
    val currentLanguage = uiState.currentLanguage!!
    val currentTheme = uiState.currentTheme!!
    val currentRole = userProfile.roles.firstOrNull() ?: UserRole.ATHLETE

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLow) // Section layer for base background
    ) {
        AppHeader(title = stringResource(Res.string.settings_title))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = spacing.screenHorizontal)
        ) {
            Spacer(modifier = Modifier.height(spacing.large))

            // Profile Section
            SectionHeader(title = stringResource(Res.string.settings_section_profile))
            ProfileCard(
                name = userProfile.displayName,
                email = userProfile.email,
                role = stringResource(Res.string.settings_role_athlete), // TODO: Get from profile
                onEditClick = { showEditDialog = true }
            )

            Spacer(modifier = Modifier.height(spacing.large))

            // Activity Section
            SectionHeader(title = stringResource(Res.string.settings_section_activity))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp), // rounded-md for serious athletic tone
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest), // Component layer - "Active Card"
                elevation = CardDefaults.cardElevation(defaultElevation = spacing.cardElevation)
            ) {
                SettingsRowItem(
                    icon = Icons.Default.History,
                    iconContainerColor = Color(0xFFDBEAFE),
                    iconTint = Color(0xFF3B82F6),
                    title = stringResource(Res.string.settings_booking_history),
                    subtitle = stringResource(Res.string.settings_sessions_attended, 24),
                    onClick = { /* TODO */ }
                )
            }

            Spacer(modifier = Modifier.height(spacing.large))

            // Preferences Section
            SectionHeader(title = stringResource(Res.string.settings_section_preferences))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp), // rounded-md for serious athletic tone
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest), // Component layer - "Active Card"
                elevation = CardDefaults.cardElevation(defaultElevation = spacing.cardElevation)
            ) {
                Column {
                    SettingsRowItem(
                        icon = Icons.Default.Language,
                        iconContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        iconTint = MaterialTheme.colorScheme.primary,
                        title = stringResource(Res.string.settings_language),
                        subtitle = currentLanguage.displayName,
                        onClick = { showLanguageSheet = true }
                    )
                    Spacer(modifier = Modifier.height(spacing.small)) // Whitespace instead of divider - "No-Line Rule"
                    SettingsRowItem(
                        icon = Icons.Default.Palette,
                        iconContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        iconTint = MaterialTheme.colorScheme.tertiary,
                        title = stringResource(Res.string.settings_dark_theme),
                        subtitle = if (currentTheme == Theme.DARK) stringResource(Res.string.settings_enabled) else stringResource(Res.string.settings_disabled),
                        trailing = {
                            Switch(
                                checked = currentTheme == Theme.DARK,
                                onCheckedChange = { viewModel.setTheme(if (it) Theme.DARK else Theme.LIGHT) }
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(spacing.small)) // Whitespace instead of divider - "No-Line Rule"
                    SettingsRowItem(
                        icon = Icons.Default.Notifications,
                        iconContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        iconTint = MaterialTheme.colorScheme.secondary,
                        title = stringResource(Res.string.settings_notifications),
                        subtitle = stringResource(Res.string.settings_notifications_description),
                        onClick = { showNotificationsSheet = true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(spacing.large))

            // Developer Options Section — debug builds only
            if (isDebug) {
                SectionHeader(title = stringResource(Res.string.settings_section_developer))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp), // rounded-md for serious athletic tone
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest), // Component layer - "Active Card"
                    elevation = CardDefaults.cardElevation(defaultElevation = spacing.cardElevation)
                ) {
                    SettingsRowItem(
                        icon = Icons.Default.Code,
                        iconContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        iconTint = MaterialTheme.colorScheme.primary,
                        title = stringResource(Res.string.settings_developer_options),
                        subtitle = stringResource(Res.string.settings_developer_options_description),
                        onClick = { showDeveloperSheet = true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(spacing.extraLarge))

            // Logout Button
            OutlinedButton(
                onClick = { viewModel.logout() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(6.dp), // rounded-md for serious athletic tone
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f)) // Ghost border style
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.Logout, null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(stringResource(Res.string.settings_logout), fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(spacing.huge))
        }
    }

    // Sheets/Dialogs
    if (showLanguageSheet) {
        LanguageBottomSheet(
            currentLanguage = currentLanguage,
            onLanguageSelected = { viewModel.setLanguage(it); showLanguageSheet = false },
            onDismiss = { showLanguageSheet = false }
        )
    }

    if (showEditDialog) {
        ProfileEditBottomSheet(
            editState = editState,
            validationErrors = uiState.validationErrors,
            onDisplayNameChange = viewModel::updateDisplayName,
            onEmailChange = viewModel::updateEmail,
            onPhoneNumberChange = viewModel::updatePhoneNumber,
            onSave = { viewModel.saveProfile { showEditDialog = false } },
            onDismiss = { viewModel.cancelEdit(); showEditDialog = false }
        )
    }

    if (showNotificationsSheet) {
        NotificationsBottomSheet(
            onDismiss = { showNotificationsSheet = false }
        )
    }

    if (showDeveloperSheet) {
        DeveloperOptionsBottomSheet(
            currentRole = currentRole,
            onRoleSelected = { role ->
                viewModel.setRole(role)
                showDeveloperSheet = false
            },
            onDismiss = { showDeveloperSheet = false }
        )
    }
}

@Composable
private fun ProfileCard(
    name: String,
    email: String,
    role: String,
    onEditClick: () -> Unit
) {
    val spacing = LocalSpacing.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(6.dp), // rounded-md for serious athletic tone
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest), // Component layer - "Active Card"
        elevation = CardDefaults.cardElevation(defaultElevation = spacing.cardElevation)
    ) {
        Column(modifier = Modifier.padding(spacing.standard)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar placeholder
                Box(
                    modifier = Modifier.size(80.dp).background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(modifier = Modifier.width(spacing.standard))
                Column {
                    Text(text = name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(text = email, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(6.dp) // rounded-md consistency
                    ) {
                        Text(
                            text = role,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(spacing.large))
            
            OutlinedButton(
                onClick = onEditClick,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(6.dp) // rounded-md for serious athletic tone
            ) {
                Icon(Icons.Default.Edit, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(Res.string.settings_edit_profile))
            }
        }
    }
}

// SettingsItemCard and SettingsRowItem removed — replaced by shared components from core/ui
