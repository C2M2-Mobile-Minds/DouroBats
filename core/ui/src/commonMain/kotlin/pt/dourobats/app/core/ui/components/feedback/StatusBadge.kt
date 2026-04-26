package pt.dourobats.app.core.ui.components.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import org.jetbrains.compose.ui.tooling.preview.Preview
import pt.dourobats.app.core.ui.theme.AppTheme
import pt.dourobats.app.core.ui.theme.proIndigo
import pt.dourobats.app.core.ui.theme.proIndigoContainer

/**
 * Kinetic Precision Status Chip
 *
 * Following the design system's chip guidelines:
 * - Attendance: Use secondaryContainer (Mint #DCFCE7) / onSecondaryContainer (Forest #166534)
 * - Skill Level: Use tonal scale (Beginner=Primary, Intermediate=Secondary, Elite=Tertiary)
 * - Rounded corners: rounded-md (6dp) to avoid "full" rounding for serious athletic tone
 *
 * Usage:
 * ```kotlin
 * // Attendance chip — Mint bg, Forest text
 * StatusBadge(
 *     text = "Attending",
 *     chipType = ChipType.POSITIVE
 * )
 *
 * // Skill level chip
 * StatusBadge(
 *     text = "Intermediate",
 *     chipType = ChipType.TIER_MID
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
        ChipType.POSITIVE  -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        ChipType.TIER_LOW  -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        ChipType.TIER_MID  -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        ChipType.TIER_HIGH -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        ChipType.NEGATIVE  -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
        ChipType.PROMINENT -> proIndigoContainer to proIndigo
        null               -> (color ?: MaterialTheme.colorScheme.surfaceVariant) to (textColor ?: MaterialTheme.colorScheme.onSurfaceVariant)
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
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

/**
 * Predefined chip types following Kinetic Precision design system.
 *
 * - POSITIVE: Secondary container — positive/attending state
 * - TIER_LOW: Primary container — low/beginner tier
 * - TIER_MID: Secondary container — mid/intermediate tier
 * - TIER_HIGH: Tertiary container — high/elite tier
 * - NEGATIVE: Error container — capacity warnings or negative states
 * - PROMINENT: Tertiary container — urgent/important actions
 */
enum class ChipType {
    POSITIVE,
    TIER_LOW,
    TIER_MID,
    TIER_HIGH,
    NEGATIVE,
    PROMINENT
}

@Preview(showBackground = true)
@Composable
private fun StatusBadgeAllVariantsPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            StatusBadge(text = "Attending", chipType = ChipType.POSITIVE)
            StatusBadge(text = "Beginner", chipType = ChipType.TIER_LOW)
            StatusBadge(text = "Intermediate", chipType = ChipType.TIER_MID)
            StatusBadge(text = "Elite", chipType = ChipType.TIER_HIGH)
            StatusBadge(text = "3 spots left", chipType = ChipType.NEGATIVE)
            StatusBadge(text = "New", chipType = ChipType.PROMINENT)
        }
    }
}
