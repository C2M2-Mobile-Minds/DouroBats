package pt.dourobats.app.core.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

/**
 * Standard 1dp outline border using [MaterialTheme.colorScheme.outlineVariant] at 50% opacity.
 * Use wherever card/surface borders need a subtle separation line.
 */
@Composable
fun subtleOutlineBorder(): BorderStroke =
    BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
