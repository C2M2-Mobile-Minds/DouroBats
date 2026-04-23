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
 * Kinetic Precision Section Card
 *
 * Reusable card container following the "No-Line Rule" and tonal layering principles.
 *
 * ## Design Principles
 * - Uses surfaceContainerLowest for the "Active Card" layer
 * - Rounded corners (rounded-md = 6dp) for serious athletic tone
 * - Tonal elevation through background shifts, not heavy shadows
 * - Responsive padding using design tokens
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
 * @param cornerRadius Corner radius for the card (default: 6dp for rounded-md)
 * @param elevation Elevation for the card (default: cardElevation from spacing)
 * @param content Card content
 */
@Composable
fun SectionCard(
    title: String? = null,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 6.dp, // rounded-md for serious athletic tone
    elevation: Dp? = null, // Will use spacing.cardElevation by default
    content: @Composable ColumnScope.() -> Unit
) {
    val spacing = LocalSpacing.current
    val cardElevation = elevation ?: spacing.cardElevation

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest // Component layer - "Active Card"
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = cardElevation
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
                    modifier = Modifier.padding(bottom = spacing.medium)
                )
            }

            // Card content
            content()
        }
    }
}
