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
        style = MaterialTheme.typography.titleLarge.copy(letterSpacing = (-0.5).sp),
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier.padding(bottom = spacing.small),
    )
}

@Preview(showBackground = true)
@Composable
private fun SectionTitlePreview() {
    AppTheme {
        SectionTitle(
            title = "This Week",
            modifier = Modifier.padding(16.dp),
        )
    }
}
