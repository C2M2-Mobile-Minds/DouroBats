package pt.dourobats.app.core.ui.components.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.components.actions.DouroButton
import pt.dourobats.app.core.ui.components.primitives.IconAvatar
import pt.dourobats.app.core.ui.theme.AppTheme

/**
 * Kinetic Precision Empty State
 *
 * A high-end empty state that communicates "nothing here yet" without making
 * the athlete feel like something is broken. Uses [IconAvatar] for the visual
 * centrepiece, Lexend (via headlineSmall) for the title, and Manrope (via
 * bodyMedium) for the calm, instructional description.
 *
 * ## Design Rules
 * - Never show a blank screen — always provide a "next step" via the CTA.
 * - The icon container uses `surfaceContainerHigh` so it visually lifts off
 *   the page without needing an explicit border.
 * - The description uses `onSurfaceVariant` (lower contrast) to de-emphasise
 *   the supporting copy and focus the eye on the title + action button.
 *
 * @param title Short, empathetic title (e.g., "No sessions today").
 * @param description One-sentence helper explaining what the athlete can do.
 * @param icon An [ImageVector] representing the empty context (calendar, whistle, etc.).
 * @param modifier Optional modifier — defaults to filling the available space.
 * @param actionLabel Optional CTA button label. Pass null to omit the button entirely.
 * @param onAction Called when the CTA button is tapped. Ignored when [actionLabel] is null.
 */
@Composable
fun DouroEmptyState(
    title: String,
    description: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        IconAvatar(
            icon = icon,
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
            size = 80.dp,
            iconSize = 40.dp,
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall, // Lexend
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium, // Manrope
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(32.dp))
            DouroButton(
                text = actionLabel,
                onClick = onAction,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DouroEmptyStateWithActionPreview() {
    AppTheme {
        DouroEmptyState(
            icon = Icons.Default.CalendarMonth,
            title = "No sessions today",
            description = "There are no training sessions scheduled for this date. Try another day or check back later.",
            actionLabel = "Browse all sessions",
            onAction = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DouroEmptyStateNoActionPreview() {
    AppTheme {
        DouroEmptyState(
            icon = Icons.Default.CalendarMonth,
            title = "No bookings yet",
            description = "Sessions you book will appear here.",
        )
    }
}
