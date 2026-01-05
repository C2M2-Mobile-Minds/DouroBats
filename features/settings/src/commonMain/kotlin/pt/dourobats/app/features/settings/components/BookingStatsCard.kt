package pt.dourobats.app.features.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import pt.dourobats.app.core.ui.theme.LocalSpacing

/**
 * Displays booking statistics in a row layout.
 *
 * Shows three key metrics:
 * - Sessions Attended
 * - Sessions Canceled
 * - Upcoming Sessions
 *
 * Currently uses mock data. In the future, this will be connected
 * to the Session repository to display real user statistics.
 *
 * @param modifier Optional modifier for the component
 */
@Composable
fun BookingStatsCard(modifier: Modifier = Modifier) {
    val spacing = LocalSpacing.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = spacing.small),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(
            number = "12",
            label = "Attended"
        )
        StatItem(
            number = "2",
            label = "Canceled"
        )
        StatItem(
            number = "3",
            label = "Upcoming"
        )
    }
}

/**
 * Individual stat display with large number and small label.
 *
 * @param number The statistic value to display
 * @param label The label describing the statistic
 * @param modifier Optional modifier
 */
@Composable
private fun StatItem(
    number: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = number,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
