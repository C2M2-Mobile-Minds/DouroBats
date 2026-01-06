package pt.dourobats.app.features.schedule.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalTime
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.schedule.data.SessionDisplayData
import java.time.format.DateTimeFormatter

/**
 * Session card component displaying session details.
 *
 * Shows session information including sport, time, location, and capacity
 * with a "Booked" badge for user's booked sessions.
 *
 * @param sessionData Session data to display
 * @param modifier Optional modifier for the card
 */
@Composable
fun SessionCard(
    sessionData: SessionDisplayData,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val session = sessionData.session

    // Calculate available spots
    val availableSpots = session.capacity - session.currentAttendees
    val isFull = availableSpots <= 0

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(spacing.standard)
        ) {
            // Header: Sport icon, name, and booking badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sport info
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

                // Booking status badge
                if (sessionData.isUserBooked) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = spacing.small, vertical = spacing.extraSmall)
                    ) {
                        Text(
                            text = "Booked",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(spacing.small))

            // Time
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.extraSmall)
            ) {
                Text(
                    text = "🕐",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = formatSessionTime(session.dateTime.time, session.duration.inWholeMinutes.toInt()),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(spacing.extraSmall))

            // Location
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.extraSmall)
            ) {
                Text(
                    text = "📍",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = sessionData.venueName,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(spacing.extraSmall))

            // Capacity
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.extraSmall)
            ) {
                Text(
                    text = "👥",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = if (isFull) "Full (${session.currentAttendees}/${session.capacity})"
                           else "$availableSpots spots available (${session.currentAttendees}/${session.capacity})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isFull) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Formats session time as "HH:MM - HH:MM" (e.g., "18:00 - 20:00")
 */
private fun formatSessionTime(startTime: LocalTime, durationMinutes: Int): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val start = startTime.toJavaLocalTime()
    val end = start.plusMinutes(durationMinutes.toLong())
    return "${start.format(formatter)} - ${end.format(formatter)}"
}
