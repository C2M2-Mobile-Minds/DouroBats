package pt.dourobats.app.core.ui.components.primitives

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
import pt.dourobats.app.core.ui.theme.LocalSpacing

/**
 * Design-system primitive: a leading icon + arbitrary content row.
 *
 * Use as a building block for metadata rows across any feature (sessions,
 * profile info, settings, version display, etc.).
 *
 * @param icon Leading icon.
 * @param iconTint Tint applied to the icon (defaults to onSurfaceVariant at 70% alpha).
 * @param contentDescription Accessibility label for the icon. Pass a descriptive string when
 *   the surrounding text alone doesn't convey the icon's meaning (e.g., "Time" for a clock icon).
 *   Leave null when the icon is purely decorative alongside self-descriptive text.
 * @param content Slot for the row's body — typically a [Text], but accepts any composable.
 */
@Composable
fun IconLabelRow(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
    contentDescription: String? = null,
    content: @Composable () -> Unit
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = modifier.padding(vertical = spacing.extraSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(16.dp),
            tint = iconTint
        )
        Spacer(modifier = Modifier.width(spacing.small))
        content()
    }
}

/**
 * Convenience overload for simple icon + text rows.
 *
 * @param icon Leading icon.
 * @param text Label text.
 * @param color Icon tint and text colour. Defaults to [MaterialTheme.colorScheme.onSurfaceVariant].
 * @param contentDescription Accessibility label for the icon. See [IconLabelRow] slot overload.
 */
@Composable
fun IconLabelRow(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    contentDescription: String? = null,
) {
    val resolvedColor = color ?: MaterialTheme.colorScheme.onSurfaceVariant
    IconLabelRow(
        icon = icon,
        modifier = modifier,
        iconTint = resolvedColor.copy(alpha = 0.7f),
        contentDescription = contentDescription
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = resolvedColor
        )
    }
}
