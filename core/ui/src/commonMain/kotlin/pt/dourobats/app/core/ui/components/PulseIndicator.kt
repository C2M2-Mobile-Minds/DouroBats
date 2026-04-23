package pt.dourobats.app.core.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pt.dourobats.app.core.ui.theme.athleticSpring

/**
 * Pulse Indicator - Signature component for live games or active sessions.
 *
 * From the Kinetic Precision Design System:
 * "For live games or active sessions, use a small 8px circle of secondary with
 * a CSS animation: a repeating scale-out of secondary_container at 30% opacity."
 *
 * This creates a breathing, energetic indicator that draws attention to live content.
 *
 * Usage:
 * ```kotlin
 * Row {
 *     PulseIndicator()
 *     Spacer(modifier = Modifier.width(8.dp))
 *     Text("LIVE")
 * }
 * ```
 *
 * @param modifier Modifier for the entire indicator
 * @param dotColor Color of the central dot (defaults to secondary "Pitch Green")
 * @param pulseColor Color of the pulse ring (defaults to secondaryContainer at 30% opacity)
 * @param dotSize Size of the central dot (default 8dp)
 * @param pulseDuration Duration of one pulse cycle in milliseconds (default 1500ms)
 */
@Composable
fun PulseIndicator(
    modifier: Modifier = Modifier,
    dotColor: Color = MaterialTheme.colorScheme.secondary,
    pulseColor: Color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
    dotSize: Dp = 8.dp,
    pulseDuration: Int = 1500
) {
    // Infinite pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    // Scale animation: 1.0 -> 2.5 -> 1.0
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = pulseDuration,
                easing = athleticSpring
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseScale"
    )

    // Opacity animation: 0.3 -> 0.0
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = pulseDuration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = modifier.size(dotSize * 3), // Ensure space for pulse to expand
        contentAlignment = Alignment.Center
    ) {
        // Pulse ring (animated scale-out effect)
        Box(
            modifier = Modifier
                .size(dotSize)
                .scale(pulseScale)
                .background(
                    color = pulseColor.copy(alpha = pulseAlpha),
                    shape = CircleShape
                )
        )

        // Central dot (static)
        Box(
            modifier = Modifier
                .size(dotSize)
                .background(
                    color = dotColor,
                    shape = CircleShape
                )
        )
    }
}

/**
 * Compact Pulse Indicator - Smaller variant for inline use.
 *
 * Same as PulseIndicator but with a 6dp dot size and tighter pulse.
 *
 * Usage:
 * ```kotlin
 * Row(verticalAlignment = Alignment.CenterVertically) {
 *     CompactPulseIndicator()
 *     Text("Live", style = MaterialTheme.typography.labelSmall)
 * }
 * ```
 */
@Composable
fun CompactPulseIndicator(
    modifier: Modifier = Modifier,
    dotColor: Color = MaterialTheme.colorScheme.secondary,
    pulseColor: Color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
) {
    PulseIndicator(
        modifier = modifier,
        dotColor = dotColor,
        pulseColor = pulseColor,
        dotSize = 6.dp,
        pulseDuration = 1200
    )
}
