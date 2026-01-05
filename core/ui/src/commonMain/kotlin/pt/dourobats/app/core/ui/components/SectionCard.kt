package pt.dourobats.app.core.ui.components

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pt.dourobats.app.core.ui.theme.LocalSpacing

/**
 * Reusable card container for grouping related content.
 *
 * Generic, isolated component that can be used across any feature.
 * Provides consistent styling with Material Design 3.
 *
 * ## Features
 * - Rounded corners (configurable)
 * - Subtle elevation (configurable)
 * - Surface color background
 * - Responsive padding using design tokens
 * - Optional section title
 *
 * ## Usage
 *
 * ```kotlin
 * SectionCard(title = "User Information") {
 *     Text("Name: John Doe")
 *     Text("Email: john@example.com")
 * }
 * ```
 *
 * @param title Optional section title displayed at the top of the card
 * @param modifier Optional modifier for the card
 * @param cornerRadius Corner radius for the card (default: 12.dp)
 * @param elevation Elevation for the card (default: 1.dp)
 * @param content Card content
 */
@Composable
fun SectionCard(
    title: String? = null,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    elevation: Dp = 1.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val spacing = LocalSpacing.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation
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
