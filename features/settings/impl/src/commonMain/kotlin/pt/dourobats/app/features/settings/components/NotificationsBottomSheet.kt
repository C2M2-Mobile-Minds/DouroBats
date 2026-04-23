package pt.dourobats.app.features.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dourobats.features.settings.generated.resources.Res
import dourobats.features.settings.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import pt.dourobats.app.core.ui.components.SectionHeader
import pt.dourobats.app.core.ui.components.SettingsRowItem
import pt.dourobats.app.core.ui.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val spacing = LocalSpacing.current

    // Local toggle states — will be backed by a ViewModel/DataStore in the future
    var sessionReminders by remember { mutableStateOf(true) }
    var bookingConfirmations by remember { mutableStateOf(true) }
    var newSessions by remember { mutableStateOf(false) }
    var announcements by remember { mutableStateOf(true) }
    var sessionCancellations by remember { mutableStateOf(true) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow, // Section layer for bottom sheet
        dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = spacing.standard)
                .padding(bottom = spacing.huge)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(Res.string.settings_notifications_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = spacing.standard)
            )

            Spacer(modifier = Modifier.height(spacing.small))

            // Sessions section
            SectionHeader(stringResource(Res.string.settings_notifications_section_sessions))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp), // rounded-md for serious athletic tone
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest), // Component layer - "Active Card"
                elevation = CardDefaults.cardElevation(defaultElevation = spacing.cardElevation)
            ) {
                Column {
                    SettingsRowItem(
                        icon = Icons.Default.NotificationsActive,
                        iconContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        iconTint = MaterialTheme.colorScheme.secondary,
                        title = stringResource(Res.string.settings_notifications_session_reminders),
                        subtitle = stringResource(Res.string.settings_notifications_session_reminders_desc),
                        trailing = { Switch(checked = sessionReminders, onCheckedChange = { sessionReminders = it }) }
                    )
                    Spacer(modifier = Modifier.height(spacing.small)) // Whitespace instead of divider - "No-Line Rule"
                    SettingsRowItem(
                        icon = Icons.Default.CheckCircle,
                        iconContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        iconTint = MaterialTheme.colorScheme.secondary,
                        title = stringResource(Res.string.settings_notifications_booking_confirmed),
                        subtitle = stringResource(Res.string.settings_notifications_booking_confirmed_desc),
                        trailing = { Switch(checked = bookingConfirmations, onCheckedChange = { bookingConfirmations = it }) }
                    )
                    Spacer(modifier = Modifier.height(spacing.small)) // Whitespace instead of divider - "No-Line Rule"
                    SettingsRowItem(
                        icon = Icons.Default.CalendarMonth,
                        iconContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        iconTint = MaterialTheme.colorScheme.primary,
                        title = stringResource(Res.string.settings_notifications_new_sessions),
                        subtitle = stringResource(Res.string.settings_notifications_new_sessions_desc),
                        trailing = { Switch(checked = newSessions, onCheckedChange = { newSessions = it }) }
                    )
                    Spacer(modifier = Modifier.height(spacing.small)) // Whitespace instead of divider - "No-Line Rule"
                    SettingsRowItem(
                        icon = Icons.Default.EventBusy,
                        iconContainerColor = MaterialTheme.colorScheme.errorContainer,
                        iconTint = MaterialTheme.colorScheme.error,
                        title = stringResource(Res.string.settings_notifications_session_cancelled),
                        subtitle = stringResource(Res.string.settings_notifications_session_cancelled_desc),
                        trailing = { Switch(checked = sessionCancellations, onCheckedChange = { sessionCancellations = it }) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(spacing.large))

            // Club section
            SectionHeader(stringResource(Res.string.settings_notifications_section_club))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp), // rounded-md for serious athletic tone
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest), // Component layer - "Active Card"
                elevation = CardDefaults.cardElevation(defaultElevation = spacing.cardElevation)
            ) {
                SettingsRowItem(
                    icon = Icons.Default.Campaign,
                    iconContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    iconTint = MaterialTheme.colorScheme.primary,
                    title = stringResource(Res.string.settings_notifications_announcements),
                    subtitle = stringResource(Res.string.settings_notifications_announcements_desc),
                    trailing = { Switch(checked = announcements, onCheckedChange = { announcements = it }) }
                )
            }
        }
    }
}
