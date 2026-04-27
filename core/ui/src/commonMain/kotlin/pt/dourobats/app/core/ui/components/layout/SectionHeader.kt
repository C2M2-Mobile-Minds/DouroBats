package pt.dourobats.app.core.ui.components.layout

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.core.ui.theme.LocalSpacing

/**
 * Subdued section label used above grouped settings rows or notification categories.
 * Style: labelLarge, bold, onSurfaceVariant.
 */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    showTopSpacing: Boolean = true,
) {
    val spacing = LocalSpacing.current
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 1.2.sp),
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.padding(top = if (showTopSpacing) spacing.large else 0.dp, bottom = spacing.small, start = 4.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun SectionHeaderPreview() {
    AppTheme {
        SectionHeader(
            title = "Upcoming Sessions",
            modifier = Modifier.padding(16.dp),
        )
    }
}
