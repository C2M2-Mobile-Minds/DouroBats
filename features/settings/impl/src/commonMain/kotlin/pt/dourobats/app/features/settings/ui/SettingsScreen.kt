package pt.dourobats.app.features.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dourobats.features.settings.generated.resources.Res
import dourobats.features.settings.generated.resources.settings_booking_history
import dourobats.features.settings.generated.resources.settings_committee_manage_sessions
import dourobats.features.settings.generated.resources.settings_committee_manage_sessions_description
import dourobats.features.settings.generated.resources.settings_committee_settings
import dourobats.features.settings.generated.resources.settings_committee_settings_description
import dourobats.features.settings.generated.resources.settings_committee_tools
import dourobats.features.settings.generated.resources.settings_committee_view_reports
import dourobats.features.settings.generated.resources.settings_committee_view_reports_description
import dourobats.features.settings.generated.resources.settings_dark_theme
import dourobats.features.settings.generated.resources.settings_developer_options
import dourobats.features.settings.generated.resources.settings_developer_options_description
import dourobats.features.settings.generated.resources.settings_disabled
import dourobats.features.settings.generated.resources.settings_enabled
import dourobats.features.settings.generated.resources.settings_language
import dourobats.features.settings.generated.resources.settings_logout
import dourobats.features.settings.generated.resources.settings_notifications
import dourobats.features.settings.generated.resources.settings_notifications_description
import dourobats.features.settings.generated.resources.settings_role_athlete
import dourobats.features.settings.generated.resources.settings_role_committee
import dourobats.features.settings.generated.resources.settings_role_supporter
import dourobats.features.settings.generated.resources.settings_section_activity
import dourobats.features.settings.generated.resources.settings_section_developer
import dourobats.features.settings.generated.resources.settings_section_preferences
import dourobats.features.settings.generated.resources.settings_sessions_attended
import dourobats.features.settings.generated.resources.settings_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pt.dourobats.app.core.ui.components.actions.DouroDestructiveButton
import pt.dourobats.app.core.ui.components.layout.AppHeader
import pt.dourobats.app.core.ui.components.layout.SectionHeader
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.login.api.model.UserRole
import pt.dourobats.app.features.settings.api.model.Theme
import pt.dourobats.app.features.settings.ui.components.DeveloperOptionsBottomSheet
import pt.dourobats.app.features.settings.ui.components.LanguageBottomSheet
import pt.dourobats.app.features.settings.ui.components.ProfileCard
import pt.dourobats.app.features.settings.ui.components.ProfileEditBottomSheet
import pt.dourobats.app.features.settings.ui.components.SettingsRowItem

@Composable
internal fun SettingsRoute(
    onNavigateToNotifications: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: SettingsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val editState by viewModel.editState.collectAsStateWithLifecycle()
    var showEditDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SettingsEvent.ProfileSaved -> showEditDialog = false
            }
        }
    }

    SettingsScreen(
        uiState = uiState,
        editState = editState,
        showEditDialog = showEditDialog,
        onShowEditDialog = { showEditDialog = true },
        onHideEditDialog = { showEditDialog = false },
        onAction = { action ->
            when (action) {
                is SettingsAction.SetLanguage -> viewModel.setLanguage(action.language)
                is SettingsAction.SetTheme -> viewModel.setTheme(action.theme)
                is SettingsAction.UpdateDisplayName -> viewModel.updateDisplayName(action.displayName)
                is SettingsAction.UpdateEmail -> viewModel.updateEmail(action.email)
                is SettingsAction.UpdatePhoneNumber -> viewModel.updatePhoneNumber(action.phoneNumber)
                is SettingsAction.SaveProfile -> viewModel.saveProfile()
                is SettingsAction.CancelEdit -> viewModel.cancelEdit()
                is SettingsAction.SetRole -> viewModel.setRole(action.role)
                is SettingsAction.Logout -> viewModel.logout()
            }
        },
        onNavigateToNotifications = onNavigateToNotifications,
        modifier = modifier,
    )
}

@Composable
internal fun SettingsScreen(
    uiState: SettingsUiState,
    editState: ProfileEditState,
    showEditDialog: Boolean,
    onShowEditDialog: () -> Unit,
    onHideEditDialog: () -> Unit,
    onAction: (SettingsAction) -> Unit,
    onNavigateToNotifications: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    var showLanguageSheet by remember { mutableStateOf(false) }
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

    val roleLabel = when (currentRole) {
        UserRole.ATHLETE -> stringResource(Res.string.settings_role_athlete)
        UserRole.SUPPORTER -> stringResource(Res.string.settings_role_supporter)
        UserRole.COMMITTEE -> stringResource(Res.string.settings_role_committee)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Content column comes AFTER AppHeader in code — Compose paints later siblings on top.
        // AppHeader shadowElevation = 0 ensures no elevation z-fighting. Hero cards use 8dp.
        AppHeader(
            title = stringResource(Res.string.settings_title),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .zIndex(1f)
                .verticalScroll(rememberScrollState()) // scroll inside the shifted viewport
                .padding(horizontal = spacing.screenHorizontal)
        ) {
            // Small top breathing room between header text and the floating hero card
            Spacer(modifier = Modifier.height(spacing.medium))
            // Profile Card — floats visually over the header's bottom edge (no SectionHeader needed)
            ProfileCard(
                name = userProfile.displayName,
                email = userProfile.email,
                role = roleLabel,
                isCommittee = currentRole == UserRole.COMMITTEE,
                onEditClick = { onShowEditDialog() }
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

            // Committee Management Hub — only visible to committee members
            if (currentRole == UserRole.COMMITTEE) {
                SectionHeader(title = stringResource(Res.string.settings_committee_tools))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = spacing.cardElevation)
                ) {
                    Column {
                        SettingsRowItem(
                            icon = Icons.Default.AdminPanelSettings,
                            iconContainerColor = MaterialTheme.colorScheme.primary,
                            iconTint = MaterialTheme.colorScheme.onPrimary,
                            title = stringResource(Res.string.settings_committee_manage_sessions),
                            subtitle = stringResource(Res.string.settings_committee_manage_sessions_description),
                            onClick = { /* TODO: Navigate to admin management */ }
                        )
                        Spacer(modifier = Modifier.height(spacing.small))
                        SettingsRowItem(
                            icon = Icons.Default.BarChart,
                            iconContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            iconTint = MaterialTheme.colorScheme.primary,
                            title = stringResource(Res.string.settings_committee_view_reports),
                            subtitle = stringResource(Res.string.settings_committee_view_reports_description),
                            onClick = { /* TODO */ }
                        )
                        Spacer(modifier = Modifier.height(spacing.small))
                        SettingsRowItem(
                            icon = Icons.Default.Settings,
                            iconContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            iconTint = MaterialTheme.colorScheme.primary,
                            title = stringResource(Res.string.settings_committee_settings),
                            subtitle = stringResource(Res.string.settings_committee_settings_description),
                            onClick = { /* TODO */ }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(spacing.large))
            }

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
                                onCheckedChange = { onAction(SettingsAction.SetTheme(if (it) Theme.DARK else Theme.LIGHT)) }
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
                        onClick = { onNavigateToNotifications() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(spacing.large))

            // Developer Options Section — debug builds only
            if (uiState.showDeveloperOptions) {
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
            DouroDestructiveButton(
                text = stringResource(Res.string.settings_logout),
                onClick = { onAction(SettingsAction.Logout) },
                icon = Icons.AutoMirrored.Filled.Logout,
            )
        }
    }

    // Sheets/Dialogs
    if (showLanguageSheet) {
        LanguageBottomSheet(
            currentLanguage = currentLanguage,
            onLanguageSelected = { onAction(SettingsAction.SetLanguage(it)); showLanguageSheet = false },
            onDismiss = { showLanguageSheet = false }
        )
    }

    if (showEditDialog) {
        ProfileEditBottomSheet(
            editState = editState,
            validationErrors = uiState.validationErrors,
            onDisplayNameChange = { onAction(SettingsAction.UpdateDisplayName(it)) },
            onEmailChange = { onAction(SettingsAction.UpdateEmail(it)) },
            onPhoneNumberChange = { onAction(SettingsAction.UpdatePhoneNumber(it)) },
            onSave = { onAction(SettingsAction.SaveProfile) },
            onDismiss = { onAction(SettingsAction.CancelEdit); onHideEditDialog() }
        )
    }

    if (showDeveloperSheet) {
        DeveloperOptionsBottomSheet(
            currentRole = currentRole,
            onRoleSelected = { role ->
                onAction(SettingsAction.SetRole(role))
                showDeveloperSheet = false
            },
            onDismiss = { showDeveloperSheet = false }
        )
    }
}
