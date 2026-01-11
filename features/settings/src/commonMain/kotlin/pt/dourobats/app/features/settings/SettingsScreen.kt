package pt.dourobats.app.features.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dourobats.features.settings.generated.resources.Res
import dourobats.features.settings.generated.resources.settings_booking_history
import dourobats.features.settings.generated.resources.settings_committee_manage_sessions
import dourobats.features.settings.generated.resources.settings_committee_manage_sessions_description
import dourobats.features.settings.generated.resources.settings_committee_settings
import dourobats.features.settings.generated.resources.settings_committee_settings_description
import dourobats.features.settings.generated.resources.settings_committee_tools
import dourobats.features.settings.generated.resources.settings_committee_view_reports
import dourobats.features.settings.generated.resources.settings_committee_view_reports_description
import dourobats.features.settings.generated.resources.settings_language
import dourobats.features.settings.generated.resources.settings_logout
import dourobats.features.settings.generated.resources.settings_section_preferences
import dourobats.features.settings.generated.resources.settings_theme
import dourobats.features.settings.generated.resources.settings_theme_dark
import dourobats.features.settings.generated.resources.settings_theme_light
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pt.dourobats.app.core.domain.model.Theme
import pt.dourobats.app.core.ui.components.SectionCard
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.settings.components.BookingStatsCard
import pt.dourobats.app.features.settings.components.LanguageBottomSheet
import pt.dourobats.app.features.settings.components.ProfileEditBottomSheet
import pt.dourobats.app.features.settings.components.ProfileHeader

/**
 * Modern card-based Settings screen with Material Design 3 patterns.
 *
 * Features:
 * - Card-based layout for visual grouping
 * - Segmented button for theme selection (clear Light/Dark choice)
 * - Bottom sheet for language selection (better mobile UX)
 * - Modal bottom sheet for profile editing (modern mobile pattern)
 * - Responsive spacing using design tokens
 *
 * @param modifier Optional modifier for the screen
 * @param viewModel ViewModel managing settings state and logic
 */
@OptIn(ExperimentalMaterial3Api::class)
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

    // Show loading state while data is being fetched from DataStore
    if (!uiState.isDataLoaded) {
        androidx.compose.foundation.layout.Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material3.CircularProgressIndicator()
        }
        return
    }

    // All data loaded - safe to access non-null values
    val userProfile = uiState.userProfile!!
    val currentLanguage = uiState.currentLanguage!!
    val currentTheme = uiState.currentTheme!!

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = spacing.screenHorizontal)
    ) {
        Spacer(modifier = Modifier.height(spacing.standard))

        // Profile Section (NO CARD)
        ProfileHeader(
            userProfile = userProfile,
            onEditClick = { showEditDialog = true }
        )

        Spacer(modifier = Modifier.height(spacing.sectionSpacing))

        // My Booking History Card
        SectionCard(title = stringResource(Res.string.settings_booking_history)) {
            BookingStatsCard()
        }

        Spacer(modifier = Modifier.height(spacing.sectionSpacing))

        // Preferences Card
        SectionCard(title = stringResource(Res.string.settings_section_preferences)) {
            // Language selector
            SettingsClickableItem(
                icon = Icons.Default.Language,
                title = stringResource(Res.string.settings_language),
                subtitle = currentLanguage.displayName,
                onClick = { showLanguageSheet = true }
            )

            Spacer(modifier = Modifier.height(spacing.standard))

            // Theme selector with SegmentedButton
            Column(modifier = Modifier.padding(vertical = spacing.small)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = stringResource(Res.string.settings_theme),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(spacing.standard))
                    Text(
                        text = stringResource(Res.string.settings_theme),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(spacing.small))

                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SegmentedButton(
                        selected = currentTheme == Theme.LIGHT,
                        onClick = { viewModel.setTheme(Theme.LIGHT) },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                    ) {
                        Text(stringResource(Res.string.settings_theme_light))
                    }
                    SegmentedButton(
                        selected = currentTheme == Theme.DARK,
                        onClick = { viewModel.setTheme(Theme.DARK) },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                    ) {
                        Text(stringResource(Res.string.settings_theme_dark))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(spacing.sectionSpacing))

        // Committee Tools Card
        SectionCard(title = stringResource(Res.string.settings_committee_tools)) {
            SettingsClickableItem(
                icon = Icons.Default.Event,
                title = stringResource(Res.string.settings_committee_manage_sessions),
                subtitle = stringResource(Res.string.settings_committee_manage_sessions_description),
                onClick = { /* TODO: Navigate to manage sessions */ }
            )

            Spacer(modifier = Modifier.height(spacing.standard))

            SettingsClickableItem(
                icon = Icons.Default.BarChart,
                title = stringResource(Res.string.settings_committee_view_reports),
                subtitle = stringResource(Res.string.settings_committee_view_reports_description),
                onClick = { /* TODO: Navigate to reports */ }
            )

            Spacer(modifier = Modifier.height(spacing.standard))

            SettingsClickableItem(
                icon = Icons.Default.Settings,
                title = stringResource(Res.string.settings_committee_settings),
                subtitle = stringResource(Res.string.settings_committee_settings_description),
                onClick = { /* TODO: Navigate to committee settings */ }
            )
        }

        Spacer(modifier = Modifier.height(spacing.sectionSpacing))

        // Logout Button (standalone)
        Button(
            onClick = { viewModel.logout() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(Res.string.settings_logout))
        }

        Spacer(modifier = Modifier.height(spacing.sectionSpacing))
    }

    // Language Bottom Sheet
    if (showLanguageSheet) {
        LanguageBottomSheet(
            currentLanguage = currentLanguage,
            onLanguageSelected = { language ->
                viewModel.setLanguage(language)
                showLanguageSheet = false
            },
            onDismiss = { showLanguageSheet = false }
        )
    }

    // Profile Edit Bottom Sheet
    LaunchedEffect(showEditDialog) {
        if (showEditDialog) {
            viewModel.enterEditMode()
        }
    }

    if (showEditDialog) {
        ProfileEditBottomSheet(
            editState = editState,
            validationErrors = uiState.validationErrors,
            onDisplayNameChange = viewModel::updateDisplayName,
            onEmailChange = viewModel::updateEmail,
            onPhoneNumberChange = viewModel::updatePhoneNumber,
            onSave = {
                viewModel.saveProfile(
                    onSuccess = {
                        showEditDialog = false
                    }
                )
            },
            onDismiss = {
                viewModel.cancelEdit()
                showEditDialog = false
            }
        )
    }

}

/**
 * Clickable settings item (e.g., language selector).
 */
@Composable
private fun SettingsClickableItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
