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
import pt.dourobats.app.core.ui.theme.subtleOutlineBorder

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
        containerColor = MaterialTheme.colorScheme.surface,
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
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = subtleOutlineBorder()
            ) {
                Column {
                    SettingsRowItem(
                        icon = Icons.Default.NotificationsActive,
                        iconContainerColor = Color(0xFFFEF3C7),
                        iconTint = Color(0xFFD97706),
                        title = stringResource(Res.string.settings_notifications_session_reminders),
                        subtitle = stringResource(Res.string.settings_notifications_session_reminders_desc),
                        trailing = { Switch(checked = sessionReminders, onCheckedChange = { sessionReminders = it }) }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = spacing.standard),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                    SettingsRowItem(
                        icon = Icons.Default.CheckCircle,
                        iconContainerColor = Color(0xFFDCFCE7),
                        iconTint = Color(0xFF16A34A),
                        title = stringResource(Res.string.settings_notifications_booking_confirmed),
                        subtitle = stringResource(Res.string.settings_notifications_booking_confirmed_desc),
                        trailing = { Switch(checked = bookingConfirmations, onCheckedChange = { bookingConfirmations = it }) }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = spacing.standard),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                    SettingsRowItem(
                        icon = Icons.Default.CalendarMonth,
                        iconContainerColor = Color(0xFFEDE9FE),
                        iconTint = Color(0xFF7C3AED),
                        title = stringResource(Res.string.settings_notifications_new_sessions),
                        subtitle = stringResource(Res.string.settings_notifications_new_sessions_desc),
                        trailing = { Switch(checked = newSessions, onCheckedChange = { newSessions = it }) }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = spacing.standard),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                    SettingsRowItem(
                        icon = Icons.Default.EventBusy,
                        iconContainerColor = Color(0xFFFFE4E6),
                        iconTint = Color(0xFFE11D48),
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
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = subtleOutlineBorder()
            ) {
                SettingsRowItem(
                    icon = Icons.Default.Campaign,
                    iconContainerColor = Color(0xFFDBEAFE),
                    iconTint = Color(0xFF2563EB),
                    title = stringResource(Res.string.settings_notifications_announcements),
                    subtitle = stringResource(Res.string.settings_notifications_announcements_desc),
                    trailing = { Switch(checked = announcements, onCheckedChange = { announcements = it }) }
                )
            }
        }
    }
}
