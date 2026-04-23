package pt.dourobats.app.features.schedule.api.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Small row with a leading icon and a text label.
 * Used for session metadata: time, location, participant count, etc.
 *
 * @param icon Leading icon.
 * @param text Label text.
 * @param color Icon and text colour. Defaults to [MaterialTheme.colorScheme.onSurfaceVariant].
 */
@Composable
fun DetailRow(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    color: Color? = null
) {
    val resolvedColor = color ?: MaterialTheme.colorScheme.onSurfaceVariant
    Row(
        modifier = modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = resolvedColor.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = resolvedColor
        )
    }
}
