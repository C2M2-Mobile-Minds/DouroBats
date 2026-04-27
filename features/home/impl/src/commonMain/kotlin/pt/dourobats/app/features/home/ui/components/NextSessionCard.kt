package pt.dourobats.app.features.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dourobats.features.home.generated.resources.Res
import dourobats.features.home.generated.resources.home_booked
import dourobats.features.home.generated.resources.home_next_session_coach
import dourobats.features.home.generated.resources.home_next_session_date
import dourobats.features.home.generated.resources.home_next_session_location
import dourobats.features.home.generated.resources.home_next_session_sport
import dourobats.features.home.generated.resources.home_participants
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.components.feedback.ChipType
import pt.dourobats.app.core.ui.components.feedback.PulseIndicator
import pt.dourobats.app.core.ui.components.feedback.StatusBadge
import pt.dourobats.app.core.ui.components.primitives.IconLabelRow
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.core.ui.theme.LocalSpacing
import androidx.compose.ui.draw.shadow
import pt.dourobats.app.core.ui.theme.ambientShadow

/**
 * Hero "ticket" card for the next booked session.
 *
 * Sits at offset(-32.dp) to visually float over the AppHeader gradient — the most
 * important element on the Home screen. Vertical accent bar = kinetic anchor.
 */
@Composable
internal fun NextSessionCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val spacing = LocalSpacing.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false,
                ambientColor = ambientShadow.copy(alpha = 0.10f),
                spotColor = ambientShadow.copy(alpha = 0.22f),
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            focusedElevation = 0.dp,
            hoveredElevation = 0.dp,
        ),
        onClick = onClick ?: {},
        enabled = onClick != null,
    ) {
        Column(modifier = Modifier.padding(spacing.standard)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
            ) {
                // Kinetic anchor — vertical primary bar = "this session matters"
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(4.dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp)),
                )

                Spacer(modifier = Modifier.width(spacing.standard))

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(spacing.small),
                        ) {
                            PulseIndicator(dotSize = 8.dp)
                            Text(
                                text = "10:00 – 11:30",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.ExtraBold,
                            )
                        }
                        Text(
                            text = stringResource(Res.string.home_next_session_sport),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp,
                        )
                        Text(
                            text = stringResource(Res.string.home_next_session_coach),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    StatusBadge(
                        text = stringResource(Res.string.home_booked),
                        chipType = ChipType.POSITIVE,
                    )
                }
            }

            Spacer(modifier = Modifier.height(spacing.standard))

            IconLabelRow(icon = Icons.Default.CalendarToday, text = stringResource(Res.string.home_next_session_date))
            Spacer(modifier = Modifier.height(spacing.extraSmall))
            IconLabelRow(icon = Icons.Default.LocationOn, text = stringResource(Res.string.home_next_session_location))
            Spacer(modifier = Modifier.height(spacing.extraSmall))
            IconLabelRow(
                icon = Icons.Default.Groups,
                text = stringResource(Res.string.home_participants, 5, 12),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NextSessionCardPreview() {
    AppTheme {
        NextSessionCard()
    }
}
