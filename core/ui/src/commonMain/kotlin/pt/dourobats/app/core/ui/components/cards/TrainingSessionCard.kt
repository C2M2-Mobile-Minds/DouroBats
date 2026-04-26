package pt.dourobats.app.core.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SportsVolleyball
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.components.feedback.ChipType
import pt.dourobats.app.core.ui.components.feedback.CompactPulseIndicator
import pt.dourobats.app.core.ui.components.feedback.StatusBadge
import pt.dourobats.app.core.ui.components.primitives.IconLabelRow
import pt.dourobats.app.core.ui.theme.AppTheme

/**
 * Kinetic Precision Training Session Card — the star component of DouroBats.
 *
 * Composes [SectionCard], [StatusBadge], [IconLabelRow], and [CompactPulseIndicator]
 * into a single high-information unit. Uses Lexend (via `titleLarge`) for the sport
 * name and Manrope (via `bodySmall`) for metadata to achieve editorial contrast.
 *
 * ## Tonal Layering
 * Inherits `surfaceContainerLowest` from [SectionCard], ensuring it "floats" above
 * `surfaceContainerLow` section backgrounds without needing a drop shadow.
 *
 * @param sportName Display name of the sport (e.g., "Volleyball", "Padel").
 * @param sportIcon [ImageVector] icon representing the sport (e.g., [Icons.Default.SportsVolleyball]).
 * @param time Formatted time range string (e.g., "18:00 - 19:30").
 * @param location Venue name displayed below the time.
 * @param level Skill level using [ChipType] — drives the chip colour from the design system.
 * @param levelLabel Human-readable level label (e.g., "Intermediate").
 * @param isAttending Whether the current athlete has booked this session.
 * @param isLive Whether the session is currently in progress.
 * @param capacityText Optional capacity warning shown in error colour (e.g., "3 spots left").
 *   Pass null when capacity is not a concern.
 * @param modifier Optional modifier applied to the card container.
 * @param onClick Called when the card is tapped.
 */
@Composable
fun TrainingSessionCard(
    sportName: String,
    sportIcon: ImageVector,
    time: String,
    location: String,
    level: ChipType,
    levelLabel: String,
    isAttending: Boolean,
    modifier: Modifier = Modifier,
    isLive: Boolean = false,
    capacityText: String? = null,
    onClick: () -> Unit,
) {
    SectionCard(modifier = modifier.clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                if (isLive) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 6.dp),
                    ) {
                        CompactPulseIndicator()
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "LIVE",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        imageVector = sportIcon,
                        contentDescription = sportName,
                        modifier = Modifier.size(22.dp),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = sportName,
                        style = MaterialTheme.typography.titleLarge, // Lexend
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                Spacer(Modifier.height(8.dp))

                IconLabelRow(
                    icon = Icons.Default.Schedule,
                    text = time,
                    contentDescription = "Time",
                )
                IconLabelRow(
                    icon = Icons.Default.LocationOn,
                    text = location,
                    contentDescription = "Location",
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                if (isAttending) {
                    StatusBadge(text = "Attending", chipType = ChipType.POSITIVE)
                    Spacer(Modifier.height(6.dp))
                }
                StatusBadge(text = levelLabel, chipType = level)
            }
        }

        if (capacityText != null) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = capacityText,
                style = MaterialTheme.typography.labelSmall, // Manrope
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TrainingSessionCardPreview() {
    AppTheme {
        TrainingSessionCard(
            sportName = "Volleyball",
            sportIcon = Icons.Default.SportsVolleyball,
            time = "18:00 - 19:30",
            location = "Pavilhão Municipal",
            level = ChipType.TIER_MID,
            levelLabel = "Intermediate",
            isAttending = true,
            isLive = false,
            capacityText = "3 spots left",
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TrainingSessionCardLivePreview() {
    AppTheme {
        TrainingSessionCard(
            sportName = "Padel",
            sportIcon = Icons.Default.SportsTennis,
            time = "10:00 - 11:00",
            location = "Clube de Ténis",
            level = ChipType.TIER_HIGH,
            levelLabel = "Elite",
            isAttending = false,
            isLive = true,
            onClick = {},
        )
    }
}

