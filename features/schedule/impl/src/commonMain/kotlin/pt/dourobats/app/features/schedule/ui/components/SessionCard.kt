package pt.dourobats.app.features.schedule.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import pt.dourobats.app.core.ui.components.primitives.IconLabelRow
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.schedule.api.ui.SessionUiModel

/**
 * Session card component displaying session details.
 */
@Composable
internal fun SessionCard(
    sessionData: SessionUiModel,
    showDate: Boolean = false,
    bookedBadgeText: String = "Booked",
    isLoading: Boolean = false,
    onBookSession: ((String) -> Unit)? = null,
    onCancelBooking: ((String) -> Unit)? = null,
    bookButtonText: String = "Book",
    cancelButtonText: String = "Cancel",
    attendingText: String = "attending",
    fullButtonText: String = "Full",
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val session = sessionData.session

    // Calculate available spots
    val availableSpots = session.capacity - session.currentAttendees
    val isFull = availableSpots <= 0

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = spacing.cardElevation),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    ) {
        Column(
            modifier = Modifier.padding(spacing.standard)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing.small)
                ) {
                    Text(
                        text = sessionData.sportIcon,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = sessionData.sportName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (sessionData.isUserBooked) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = spacing.small, vertical = spacing.extraSmall)
                    ) {
                        Text(
                            text = bookedBadgeText,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(spacing.medium))

            if (showDate) {
                IconLabelRow(icon = Icons.Default.CalendarToday, text = sessionData.formattedShortDate, contentDescription = "Date")
            }

            IconLabelRow(icon = Icons.Default.AccessTime, text = sessionData.formattedTimeRange, contentDescription = "Time")
            IconLabelRow(icon = Icons.Default.LocationOn, text = sessionData.venueName, contentDescription = "Location")
            IconLabelRow(
                icon = Icons.Default.Groups,
                text = "${session.currentAttendees}/${session.capacity} $attendingText",
                color = if (isFull) MaterialTheme.colorScheme.error else null,
                contentDescription = "Participants"
            )

            Spacer(modifier = Modifier.height(spacing.medium))

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                }
                sessionData.isUserBooked && onCancelBooking != null -> {
                    OutlinedButton(
                        onClick = { onCancelBooking(session.id) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(cancelButtonText)
                    }
                }
                !sessionData.isUserBooked && !isFull && onBookSession != null -> {
                    Button(
                        onClick = { onBookSession(session.id) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                    ) {
                        Text(
                            text = bookButtonText,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                !sessionData.isUserBooked && isFull -> {
                    Button(
                        onClick = { },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(fullButtonText)
                    }
                }
            }
        }
    }
}
