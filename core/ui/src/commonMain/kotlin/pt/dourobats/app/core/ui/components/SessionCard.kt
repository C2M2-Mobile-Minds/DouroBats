package pt.dourobats.app.core.ui.components

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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.core.ui.model.SessionDisplayData

/**
 * Session card component displaying session details.
 *
 * Shows session information including sport, time, location, and capacity
 * with a "Booked" badge for user's booked sessions.
 *
 * @param sessionData Session data to display
 * @param showDate Whether to display the session date (useful when showing sessions from multiple dates)
 * @param bookedBadgeText Text to display on the booked badge (default: "Booked")
 * @param modifier Optional modifier for the card
 */
@Composable
fun SessionCard(
    sessionData: SessionDisplayData,
    showDate: Boolean = false,
    bookedBadgeText: String = "Booked",
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
                            text = bookedBadgeText,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(spacing.small))

            // Date (only shown when showDate = true)
            if (showDate) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing.extraSmall)
                ) {
                    Text(
                        text = "📅",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = formatSessionDate(session.dateTime.date),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(spacing.extraSmall))
            }

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
 * Formats session date as "YYYY-MM-DD" (e.g., "2026-01-06")
 * Simple format that works across all platforms.
 */
private fun formatSessionDate(date: LocalDate): String {
    val monthName = when (date.monthNumber) {
        1 -> "Jan"
        2 -> "Feb"
        3 -> "Mar"
        4 -> "Apr"
        5 -> "May"
        6 -> "Jun"
        7 -> "Jul"
        8 -> "Aug"
        9 -> "Sep"
        10 -> "Oct"
        11 -> "Nov"
        12 -> "Dec"
        else -> ""
    }
    return "$monthName ${date.dayOfMonth}, ${date.year}"
}

/**
 * Formats session time as "HH:MM - HH:MM" (e.g., "18:00 - 20:00")
 * Simple format that works across all platforms.
 */
private fun formatSessionTime(startTime: LocalTime, durationMinutes: Int): String {
    val startHour = startTime.hour.toString().padStart(2, '0')
    val startMinute = startTime.minute.toString().padStart(2, '0')

    // Calculate end time
    val totalMinutes = startTime.hour * 60 + startTime.minute + durationMinutes
    val endHour = (totalMinutes / 60) % 24
    val endMinute = totalMinutes % 60

    val endHourStr = endHour.toString().padStart(2, '0')
    val endMinuteStr = endMinute.toString().padStart(2, '0')

    return "$startHour:$startMinute - $endHourStr:$endMinuteStr"
}
