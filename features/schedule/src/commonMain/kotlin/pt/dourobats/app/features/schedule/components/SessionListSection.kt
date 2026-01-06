package pt.dourobats.app.features.schedule.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import pt.dourobats.app.core.ui.theme.LocalSpacing
import pt.dourobats.app.features.schedule.data.SessionDisplayData

/**
 * Section component for displaying a list of sessions.
 *
 * Shows a title and either a list of session cards or an empty state message.
 * Used for both "Available Sessions" and "My Schedule" sections.
 *
 * @param title Section title (e.g., "Available Sessions", "My Schedule")
 * @param sessions List of sessions to display
 * @param emptyMessage Message to show when no sessions available
 * @param showDate Whether to show dates on session cards (useful for multi-date lists)
 * @param modifier Optional modifier for the section
 */
@Composable
fun SessionListSection(
    title: String,
    sessions: List<SessionDisplayData>,
    emptyMessage: String,
    showDate: Boolean = false,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Column(modifier = modifier.fillMaxWidth()) {
        // Section title
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = spacing.screenHorizontal)
        )

        Spacer(modifier = Modifier.height(spacing.standard))

        // Session list or empty state
        if (sessions.isEmpty()) {
            Text(
                text = emptyMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = spacing.screenHorizontal)
            )
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacing.small),
                modifier = Modifier.padding(horizontal = spacing.screenHorizontal)
            ) {
                sessions.forEach { sessionData ->
                    SessionCard(
                        sessionData = sessionData,
                        showDate = showDate
                    )
                }
            }
        }
    }
}
