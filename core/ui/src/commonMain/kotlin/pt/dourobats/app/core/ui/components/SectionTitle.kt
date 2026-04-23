package pt.dourobats.app.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import pt.dourobats.app.core.ui.theme.LocalSpacing

/**
 * Content section heading used above grouped content blocks.
 * Style: titleLarge, bold, onBackground — distinct from [SectionHeader] (labelLarge, onSurfaceVariant).
 */
@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier.padding(bottom = spacing.small),
    )
}
