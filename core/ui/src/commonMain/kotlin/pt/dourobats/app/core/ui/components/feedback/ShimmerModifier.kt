package pt.dourobats.app.core.ui.components.feedback

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

/**
 * Kinetic Precision Shimmer Effect
 *
 * A composable Modifier that overlays a sliding gradient to signal loading state.
 * Use this on any Box/Surface placeholder to create "skeleton screen" loading.
 *
 * Usage:
 * ```kotlin
 * Box(modifier = Modifier.size(120.dp, 16.dp).clip(RoundedCornerShape(4.dp)).shimmer())
 * ```
 */
@Composable
fun Modifier.shimmer(): Modifier {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = -500f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerTranslation",
    )

    val baseColor = MaterialTheme.colorScheme.surfaceVariant
    val shimmerColors = listOf(
        baseColor.copy(alpha = 0.8f),
        baseColor.copy(alpha = 0.3f),
        baseColor.copy(alpha = 0.8f),
    )

    return background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(x = translateAnim - 500f, y = translateAnim - 500f),
            end = Offset(x = translateAnim, y = translateAnim),
        )
    )
}
