package pt.dourobats.app.features.settings.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dourobats.features.settings.generated.resources.Res
import dourobats.features.settings.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.components.layout.AppHeader
import pt.dourobats.app.core.ui.components.layout.SectionHeader
import pt.dourobats.app.features.settings.ui.components.SettingsRowItem
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.core.ui.theme.LocalSpacing

@Composable
internal fun NotificationsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current

    // Local toggle states — will be backed by a ViewModel/DataStore in the future
    var sessionReminders by remember { mutableStateOf(true) }
    var bookingConfirmations by remember { mutableStateOf(true) }
    var newSessions by remember { mutableStateOf(false) }
    var announcements by remember { mutableStateOf(true) }
    var sessionCancellations by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets(0)),
    ) {
        AppHeader(
            title = stringResource(Res.string.settings_notifications_title),
            leading = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            },
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = spacing.screenHorizontal)
        ) {
            Spacer(modifier = Modifier.height(spacing.medium))

            // Sessions section
            SectionHeader(stringResource(Res.string.settings_notifications_section_sessions))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column {
                    SettingsRowItem(
                        icon = Icons.Default.NotificationsActive,
                        iconContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        iconTint = MaterialTheme.colorScheme.secondary,
                        title = stringResource(Res.string.settings_notifications_session_reminders),
                        subtitle = stringResource(Res.string.settings_notifications_session_reminders_desc),
                        trailing = { Switch(checked = sessionReminders, onCheckedChange = { sessionReminders = it }) },
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = spacing.standard),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                    )
                    SettingsRowItem(
                        icon = Icons.Default.CheckCircle,
                        iconContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        iconTint = MaterialTheme.colorScheme.secondary,
                        title = stringResource(Res.string.settings_notifications_booking_confirmed),
                        subtitle = stringResource(Res.string.settings_notifications_booking_confirmed_desc),
                        trailing = { Switch(checked = bookingConfirmations, onCheckedChange = { bookingConfirmations = it }) },
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = spacing.standard),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                    )
                    SettingsRowItem(
                        icon = Icons.Default.CalendarMonth,
                        iconContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        iconTint = MaterialTheme.colorScheme.primary,
                        title = stringResource(Res.string.settings_notifications_new_sessions),
                        subtitle = stringResource(Res.string.settings_notifications_new_sessions_desc),
                        trailing = { Switch(checked = newSessions, onCheckedChange = { newSessions = it }) },
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = spacing.standard),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                    )
                    SettingsRowItem(
                        icon = Icons.Default.EventBusy,
                        iconContainerColor = MaterialTheme.colorScheme.errorContainer,
                        iconTint = MaterialTheme.colorScheme.error,
                        title = stringResource(Res.string.settings_notifications_session_cancelled),
                        subtitle = stringResource(Res.string.settings_notifications_session_cancelled_desc),
                        trailing = { Switch(checked = sessionCancellations, onCheckedChange = { sessionCancellations = it }) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(spacing.large))

            // Club section
            SectionHeader(stringResource(Res.string.settings_notifications_section_club))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                SettingsRowItem(
                    icon = Icons.Default.Campaign,
                    iconContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    iconTint = MaterialTheme.colorScheme.primary,
                    title = stringResource(Res.string.settings_notifications_announcements),
                    subtitle = stringResource(Res.string.settings_notifications_announcements_desc),
                    trailing = { Switch(checked = announcements, onCheckedChange = { announcements = it }) },
                )
            }

            Spacer(modifier = Modifier.height(spacing.huge))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationsScreenPreview() {
    AppTheme {
        NotificationsScreen(onBack = {})
    }
}
