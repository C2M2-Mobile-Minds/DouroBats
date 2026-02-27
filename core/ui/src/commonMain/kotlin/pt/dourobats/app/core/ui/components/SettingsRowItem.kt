package pt.dourobats.app.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pt.dourobats.app.core.ui.theme.LocalSpacing

/**
 * Standard settings-style row: icon avatar | title + subtitle | trailing content.
 *
 * Used in preferences rows, notification toggles, and any settings-like list item.
 *
 * @param icon Icon to display inside the avatar container.
 * @param iconContainerColor Background colour of the icon container.
 * @param iconTint Icon tint colour.
 * @param title Primary label.
 * @param subtitle Secondary description text.
 * @param onClick Click handler for the whole row. When null, the row is not clickable
 *   (use this for rows whose interaction is handled entirely by a trailing widget like Switch).
 *   Defaults to null. When non-null and no [trailing] is provided, a [ChevronRight] is shown.
 * @param trailing Custom trailing content. Defaults to a [ChevronRight] when onClick is
 *   non-null and trailing is null; nothing when onClick is null.
 * @param iconContainerSize Size of the icon container (default 48 dp).
 * @param iconContainerShape Shape of the icon container (default [CircleShape]).
 * @param iconSize Size of the icon inside the container (default 24 dp).
 */
@Composable
fun SettingsRowItem(
    icon: ImageVector,
    iconContainerColor: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    iconContainerSize: Dp = 48.dp,
    iconContainerShape: Shape = CircleShape,
    iconSize: Dp = 24.dp
) {
    val spacing = LocalSpacing.current
    val clickableModifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(clickableModifier)
            .padding(spacing.standard),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(iconContainerSize)
                .background(iconContainerColor, iconContainerShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(iconSize), tint = iconTint)
        }
        Spacer(modifier = Modifier.width(spacing.standard))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (trailing != null) {
            trailing()
        } else if (onClick != null) {
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
