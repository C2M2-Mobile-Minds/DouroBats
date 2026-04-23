package pt.dourobats.app.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Kinetic Precision Status Chip
 *
 * Following the design system's chip guidelines:
 * - Attendance: Use secondary_container (#75ff68) for "Attending"
 * - Skill Level: Use tonal scale (Beginner=Primary, Intermediate=Secondary, Elite=Tertiary)
 * - Rounded corners: rounded-md (6dp) to avoid "full" rounding for serious athletic tone
 *
 * Usage:
 * ```kotlin
 * // Attendance chip
 * StatusBadge(
 *     text = "Attending",
 *     color = MaterialTheme.colorScheme.secondaryContainer,
 *     textColor = MaterialTheme.colorScheme.onSecondaryContainer
 * )
 *
 * // Skill level chip
 * StatusBadge(
 *     text = "Intermediate",
 *     chipType = ChipType.SKILL_INTERMEDIATE
 * )
 * ```
 *
 * @param text The text to display on the chip
 * @param color Optional custom background color
 * @param textColor Optional custom text color
 * @param chipType Optional predefined chip type for common use cases
 * @param modifier Optional modifier
 */
@Composable
fun StatusBadge(
    text: String,
    color: Color? = null,
    textColor: Color? = null,
    chipType: ChipType? = null,
    modifier: Modifier = Modifier
) {
    val (bgColor, fgColor) = when (chipType) {
        ChipType.ATTENDING -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        ChipType.SKILL_BEGINNER -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        ChipType.SKILL_INTERMEDIATE -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        ChipType.SKILL_ELITE -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        ChipType.FULL -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
        ChipType.ACTION -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer // Clay Orange for high-energy
        null -> (color ?: MaterialTheme.colorScheme.surfaceVariant) to (textColor ?: MaterialTheme.colorScheme.onSurfaceVariant)
    }

    Surface(
        modifier = modifier,
        color = bgColor,
        shape = RoundedCornerShape(6.dp) // rounded-md for serious athletic tone
    ) {
        Text(
            text = text,
            color = fgColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

/**
 * Predefined chip types following Kinetic Precision design system.
 *
 * - ATTENDING: Pitch Green (#75ff68) for positive attendance state
 * - SKILL_BEGINNER: Primary tint (Midnight Pitch container)
 * - SKILL_INTERMEDIATE: Secondary tint (Pitch Green container)
 * - SKILL_ELITE: Tertiary tint (Clay Orange container)
 * - FULL: Error container for capacity warnings
 * - ACTION: Clay Orange for urgent/important actions
 */
enum class ChipType {
    ATTENDING,
    SKILL_BEGINNER,
    SKILL_INTERMEDIATE,
    SKILL_ELITE,
    FULL,
    ACTION
}
