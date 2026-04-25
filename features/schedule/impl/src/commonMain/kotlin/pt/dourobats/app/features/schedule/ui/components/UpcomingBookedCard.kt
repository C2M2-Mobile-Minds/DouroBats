package pt.dourobats.app.features.schedule.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.schedule.api.ui.SessionUiModel

@Composable
internal fun UpcomingBookedCard(
    sessionData: SessionUiModel,
    onCancel: (String) -> Unit,
    cancelShortText: String = "Cancel"
) {
    val spacing = LocalSpacing.current
    val session = sessionData.session
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.screenHorizontal),
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = spacing.cardElevation)
    ) {
        Row(
            modifier = Modifier.padding(spacing.standard),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(sessionData.sportIcon, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(spacing.standard))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = sessionData.sportName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "${sessionData.formattedShortDate} • ${sessionData.formattedTimeRange.split(" - ")[0]} • ${sessionData.venueName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(spacing.small))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    modifier = Modifier.clickable { onCancel(session.id) }
                ) {
                    Text(
                        text = cancelShortText,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
