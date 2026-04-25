package pt.dourobats.app.core.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Kinetic Precision Theme Extensions
 *
 * ## The "No-Line Rule"
 * Borders are a failure of hierarchy. Use background shifts to define boundaries.
 * - Place a surface-container-lowest card on a surface-container-low background
 * - Use outline-variant only at 20% opacity for "Ghost Borders" when absolutely required
 *
 * ## The Glass & Gradient Rule
 * For hero sections and floating action buttons, use Glassmorphism. Apply surface-tint
 * at 10% opacity with 20px backdrop blur to let vibrant colors bleed through.
 */

/**
 * Ghost Border - Use ONLY for input fields when absolute contrast is required.
 *
 * The "No-Line Rule" states: borders are a failure of hierarchy.
 * However, input fields require a border for accessibility. This provides a 2px
 * ghost border using the primary color at 40% opacity.
 *
 * Usage:
 * ```kotlin
 * TextField(
 *     modifier = Modifier
 *         .ghostBorder(isFocused = true)
 * )
 * ```
 *
 * @param isFocused Whether the input is currently focused
 * @param color The border color (defaults to primary)
 * @return BorderStroke for ghost border effect
 */
@Composable
fun ghostBorder(isFocused: Boolean = false, color: Color? = null): BorderStroke {
    val borderColor = color ?: MaterialTheme.colorScheme.primary
    return BorderStroke(
        width = 2.dp,
        color = borderColor.copy(alpha = if (isFocused) 0.4f else 0.2f)
    )
}

/**
 * Glassmorphism Background - For hero sections and floating components.
 *
 * Creates a frosted glass effect with surface tint at 10% opacity. This ensures
 * vibrant "Pitch Green" or "Clay Orange" from the background bleeds through,
 * making the UI feel integrated into the environment.
 *
 * Usage:
 * ```kotlin
 * Box(
 *     modifier = Modifier
 *         .glassmorphism(tintColor = MaterialTheme.colorScheme.primaryContainer)
 * )
 * ```
 *
 * Note: Backdrop blur is not natively supported in Compose Multiplatform.
 * This provides the tint effect. For true blur, use platform-specific APIs.
 *
 * @param tintColor The tint color to apply (defaults to surface-container)
 * @param alpha The opacity of the tint (default 0.1f for 10%)
 */
@Composable
fun Modifier.glassmorphism(
    tintColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    alpha: Float = 0.1f
): Modifier = this.background(tintColor.copy(alpha = alpha))

/**
 * Glassmorphism Gradient - For hero sections with directional lighting.
 *
 * Creates a gradient glass effect from primary/secondary colors for editorial depth.
 *
 * Usage:
 * ```kotlin
 * Box(
 *     modifier = Modifier
 *         .glassmorphismGradient(
 *             startColor = MaterialTheme.colorScheme.primary,
 *             endColor = MaterialTheme.colorScheme.primaryContainer
 *         )
 * )
 * ```
 *
 * @param startColor The gradient start color
 * @param endColor The gradient end color
 * @param alpha The overall opacity (default 0.05f for 5%)
 */
@Composable
fun Modifier.glassmorphismGradient(
    startColor: Color = MaterialTheme.colorScheme.primary,
    endColor: Color = MaterialTheme.colorScheme.primaryContainer,
    alpha: Float = 0.05f
): Modifier = this.background(
    brush = Brush.verticalGradient(
        colors = listOf(
            startColor.copy(alpha = alpha),
            endColor.copy(alpha = alpha)
        )
    )
)

/**
 * Ambient Shadow - Soft shadow using on-surface color.
 *
 * Shadows in Kinetic Precision are not grey; they are "Ambient Tints."
 * Use the on-surface color at 4% opacity to mimic soft stadium lighting.
 *
 * Note: Compose doesn't support custom blur radius. This provides a subtle
 * background tint for the shadow effect. For true shadows with blur, use
 * elevation or platform-specific shadow APIs.
 *
 * Usage:
 * ```kotlin
 * Card(
 *     modifier = Modifier
 *         .ambientShadow()
 * )
 * ```
 *
 * @param shadowColor The shadow color (defaults to onSurface at 4% opacity)
 */
@Composable
fun Modifier.ambientShadow(
    shadowColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f)
): Modifier = this.drawBehind {
    // Draw a subtle bottom shadow rectangle
    drawRect(
        color = shadowColor,
        topLeft = androidx.compose.ui.geometry.Offset(0f, size.height - 4.dp.toPx())
    )
}

/**
 * Athletic Spring Easing
 *
 * All transitions should use this cubic-bezier(0.34, 1.56, 0.64, 1) easing
 * to give a "snappy, athletic" spring to the UI.
 *
 * Usage:
 * ```kotlin
 * animateFloatAsState(
 *     targetValue = if (expanded) 1f else 0f,
 *     animationSpec = tween(
 *         durationMillis = 300,
 *         easing = athleticSpring
 *     )
 * )
 * ```
 */
val athleticSpring = CubicBezierEasing(0.34f, 1.56f, 0.64f, 1f)

/**
 * Tonal Surface - Creates a lifted surface with proper color layering.
 *
 * Following the "No-Line Rule", use background color shifts instead of borders.
 * This modifier applies the appropriate surface container color based on the layer.
 *
 * Surface Hierarchy:
 * 1. Base Layer: surface (#f8f9fa) – The stadium floor
 * 2. Section Layer: surface-container-low (#f3f4f5) – Defines large functional areas
 * 3. Component Layer: surface-container-lowest (#ffffff) – The "Active Card"
 *
 * Usage:
 * ```kotlin
 * Card(
 *     modifier = Modifier.tonalSurface(layer = SurfaceLayer.COMPONENT),
 *     colors = CardDefaults.cardColors(
 *         containerColor = Color.Transparent // Let the modifier handle the color
 *     )
 * )
 * ```
 *
 * @param layer The surface layer (BASE, SECTION, COMPONENT)
 */
enum class SurfaceLayer {
    BASE,       // surface
    SECTION,    // surface-container-low
    COMPONENT   // surface-container-lowest
}

@Composable
fun Modifier.tonalSurface(layer: SurfaceLayer): Modifier {
    val color = when (layer) {
        SurfaceLayer.BASE -> MaterialTheme.colorScheme.surface
        SurfaceLayer.SECTION -> MaterialTheme.colorScheme.surfaceContainerLow
        SurfaceLayer.COMPONENT -> MaterialTheme.colorScheme.surfaceContainerLowest
    }
    return this.background(color)
}
