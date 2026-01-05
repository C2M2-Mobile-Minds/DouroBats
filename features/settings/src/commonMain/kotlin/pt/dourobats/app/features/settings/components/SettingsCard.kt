package pt.dourobats.app.features.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.dourobats.app.core.ui.theme.LocalSpacing

/**
 * Reusable card container for grouping related settings.
 *
 * Provides consistent styling with Material Design 3:
 * - Rounded corners (12.dp)
 * - Subtle elevation (1.dp)
 * - Surface color background
 * - Responsive padding using design tokens
 *
 * ## Usage
 *
 * ```kotlin
 * SettingsCard(title = "Preferences") {
 *     SettingsSwitchItem(...)
 *     SettingsItem(...)
 * }
 * ```
 *
 * @param title Optional section title displayed at the top of the card
 * @param modifier Optional modifier for the card
 * @param content Card content (settings items, etc.)
 */
@Composable
fun SettingsCard(
    title: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val spacing = LocalSpacing.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.cardPadding)
        ) {
            // Optional title
            title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = spacing.small)
                )
            }

            // Card content
            content()
        }
    }
}
