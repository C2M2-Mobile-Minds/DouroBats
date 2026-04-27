package pt.dourobats.app.features.home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsVolleyball
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.components.cards.SectionCard
import pt.dourobats.app.core.ui.components.primitives.IconAvatar
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.core.ui.theme.LocalSpacing

@Composable
internal fun UpcomingSessionItem(
    sport: String,
    location: String,
    time: String,
    participants: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    isFull: Boolean = false,
    onClick: () -> Unit = {},
) {
    val spacing = LocalSpacing.current

    SectionCard(modifier = modifier, onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
            ) {
                IconAvatar(
                    icon = icon,
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    iconTint = MaterialTheme.colorScheme.primary,
                    size = 48.dp,
                    iconSize = 22.dp,
                )
                Spacer(modifier = Modifier.width(spacing.standard))
                Column {
                    Text(
                        text = sport,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = "$location  •  $time",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
            Text(
                text = participants,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = if (isFull) MaterialTheme.colorScheme.outline
                        else MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UpcomingSessionItemPreview() {
    AppTheme {
        UpcomingSessionItem(
            sport = "Volleyball",
            location = "Secondary Hall",
            time = "18:00",
            participants = "10/14",
            icon = Icons.Default.SportsVolleyball,
        )
    }
}

