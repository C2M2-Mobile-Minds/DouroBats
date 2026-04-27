package pt.dourobats.app.core.ui.components.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.theme.AppTheme

/**
 * Icon inside a coloured container — used across Settings, Notifications, and Home.
 */
@Composable
fun IconAvatar(
    icon: ImageVector,
    containerColor: Color,
    iconTint: Color,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    iconSize: Dp = 24.dp,
    shape: Shape = CircleShape
) {
    Box(
        modifier = modifier
            .size(size)
            .background(containerColor, shape).clip(shape),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(iconSize), tint = iconTint)
    }
}

@Preview(showBackground = true)
@Composable
private fun IconAvatarPreview() {
    AppTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconAvatar(
                icon = Icons.Default.Person,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            IconAvatar(
                icon = Icons.Default.Settings,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
                size = 56.dp,
                iconSize = 28.dp,
            )
            IconAvatar(
                icon = Icons.Default.CalendarMonth,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                iconTint = MaterialTheme.colorScheme.onTertiaryContainer,
                size = 80.dp,
                iconSize = 40.dp,
            )
        }
    }
}

/**
 * Ghost variant of [IconAvatar] for secondary or historical information.
 *
 * Uses `surfaceContainerLow` + `onSurfaceVariant` at 80% opacity — sits quietly in the
 * tonal hierarchy without competing with primary content. Useful in history lists,
 * read-only info rows, or any context where the icon is informational rather than action-driving.
 *
 * @param icon Leading icon.
 * @param modifier Optional modifier.
 * @param size Container size (default 40dp).
 */
@Composable
fun GhostIconAvatar(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
) {
    IconAvatar(
        icon = icon,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        iconTint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
        size = size,
        iconSize = if (size > 48.dp) size * 0.45f else size * 0.5f, // Maintains the 50% icon-to-container ratio
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun GhostIconAvatarPreview() {
    AppTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GhostIconAvatar(icon = Icons.Default.CalendarMonth)
            GhostIconAvatar(icon = Icons.Default.Person, size = 48.dp)
            GhostIconAvatar(icon = Icons.Default.Settings, size = 56.dp)
        }
    }
}
