package pt.dourobats.app.features.schedule.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dourobats.features.schedule.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import pt.dourobats.app.core.ui.components.actions.DouroButton
import pt.dourobats.app.core.ui.components.actions.DouroOutlinedButton
import pt.dourobats.app.core.ui.components.cards.SectionCard
import pt.dourobats.app.core.ui.components.feedback.ChipType
import pt.dourobats.app.core.ui.components.feedback.StatusBadge
import pt.dourobats.app.core.ui.components.primitives.IconLabelRow
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.schedule.api.ui.SessionUiModel

/**
 * Unified session display card.
 *
 * @param isCompact  When true renders a compact horizontal row (used for "My Schedule" list).
 *                   When false renders a full detail card with action button (used for selected-day list).
 * @param onBook     Called with session ID when the user taps "Book".
 * @param onCancel   Called with session ID when the user taps "Cancel".
 */
@Composable
internal fun SessionCard(
    sessionData: SessionUiModel,
    onBook: (String) -> Unit,
    onCancel: (String) -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = false,
    isLoading: Boolean = false,
) {
    SectionCard(
        modifier = modifier,
        contentPadding = PaddingValues(0.dp),
        accentColor = if (sessionData.isUserBooked) MaterialTheme.colorScheme.secondary else null,
    ) {
        if (isCompact) {
            CompactSessionLayout(sessionData = sessionData, isLoading = isLoading, onCancel = onCancel)
        } else {
            FullSessionLayout(sessionData = sessionData, isLoading = isLoading, onBook = onBook, onCancel = onCancel)
        }
    }
}

@Composable
private fun CompactSessionLayout(
    sessionData: SessionUiModel,
    isLoading: Boolean,
    onCancel: (String) -> Unit,
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(spacing.standard),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = sportIdToIcon(sessionData.session.sportId),
                    contentDescription = sessionData.sportName,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp),
                )
            }
        }

        Spacer(modifier = Modifier.width(spacing.standard))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = sessionData.sportName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
            )
            Text(
                text = "${sessionData.formattedShortDate} • ${sessionData.formattedStartTime} • ${sessionData.venueName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
        } else {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.clickable { onCancel(sessionData.session.id) },
            ) {
                Text(
                    text = stringResource(Res.string.session_button_cancel_short),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        }
    }
}

@Composable
private fun FullSessionLayout(
    sessionData: SessionUiModel,
    isLoading: Boolean,
    onBook: (String) -> Unit,
    onCancel: (String) -> Unit,
) {
    val spacing = LocalSpacing.current
    val session = sessionData.session
    val isFull = session.currentAttendees >= session.capacity

    // Split "10:00 - 11:30" into start and end
    val timeParts = sessionData.formattedTimeRange.split(" - ")
    val startTime = timeParts.getOrElse(0) { sessionData.formattedTimeRange }
    val endTime = timeParts.getOrElse(1) { "" }

    Column(modifier = Modifier.padding(spacing.standard)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Time block — "When" at a glance
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(64.dp),
            ) {
                Text(
                    text = startTime,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary,
                )
                if (endTime.isNotEmpty()) {
                    Text(
                        text = endTime,
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.5.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            VerticalDivider(
                modifier = Modifier
                    .padding(horizontal = spacing.standard)
                    .fillMaxHeight(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
            )

            // Content block — "What" + status
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spacing.small),
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(
                            imageVector = sportIdToIcon(session.sportId),
                            contentDescription = sessionData.sportName,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp),
                        )
                        Text(
                            text = sessionData.sportName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    when {
                        sessionData.isUserBooked -> StatusBadge(
                            text = stringResource(Res.string.session_booked_badge),
                            chipType = ChipType.POSITIVE,
                        )
                        isFull -> StatusBadge(
                            text = stringResource(Res.string.session_full_badge),
                            chipType = ChipType.NEGATIVE,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(spacing.extraSmall))

                IconLabelRow(icon = Icons.Default.LocationOn, text = sessionData.venueName, contentDescription = "Location")
                IconLabelRow(
                    icon = Icons.Default.Groups,
                    text = stringResource(Res.string.session_participants_label, session.currentAttendees, session.capacity),
                    color = if (isFull) MaterialTheme.colorScheme.error else null,
                    contentDescription = "Participants",
                )
            }
        }

        Spacer(modifier = Modifier.height(spacing.medium))

        Box(
            modifier = Modifier.fillMaxWidth().height(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 3.dp)
            } else {
                when {
                    sessionData.isUserBooked -> DouroOutlinedButton(
                        text = stringResource(Res.string.session_button_cancel),
                        onClick = { onCancel(session.id) },
                    )
                    !isFull -> DouroButton(
                        text = stringResource(Res.string.session_button_book),
                        onClick = { onBook(session.id) },
                    )
                    else -> DouroButton(
                        text = stringResource(Res.string.session_button_full),
                        onClick = {},
                        enabled = false,
                    )
                }
            }
        }
    }
}

private fun sportIdToIcon(sportId: String): ImageVector = when (sportId) {
    "volleyball" -> Icons.Default.SportsVolleyball
    "futsal"     -> Icons.Default.SportsSoccer
    "swimming"   -> Icons.Default.Pool
    "basketball" -> Icons.Default.SportsBasketball
    "padel"      -> Icons.Default.SportsTennis
    "running"    -> Icons.AutoMirrored.Filled.DirectionsRun
    else         -> Icons.Default.FitnessCenter
}
