package pt.dourobats.app.core.ui.components.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
            .background(containerColor, shape),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(iconSize), tint = iconTint)
    }
}
