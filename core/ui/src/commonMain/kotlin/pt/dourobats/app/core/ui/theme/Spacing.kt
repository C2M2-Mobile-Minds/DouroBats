package pt.dourobats.app.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Design tokens for spacing based on Material Design's 8dp grid system.
 *
 * This spacing system provides:
 * - Consistent spacing across the entire app
 * - Responsive spacing that adapts to different screen sizes
 * - Semantic naming for better code readability
 * - Easy global adjustments (change once, updates everywhere)
 *
 * ## Usage
 *
 * ```kotlin
 * @Composable
 * fun MyScreen() {
 *     val spacing = LocalSpacing.current
 *
 *     Column(
 *         modifier = Modifier.padding(horizontal = spacing.screenHorizontal)
 *     ) {
 *         Text("Title")
 *         Spacer(modifier = Modifier.height(spacing.sectionSpacing))
 *         // Content
 *     }
 * }
 * ```
 *
 * ## Spacing Scale (8dp Grid)
 *
 * - **extraSmall (4dp)**: Micro spacing, icon padding, chips
 * - **small (8dp)**: Between related items
 * - **medium (12dp)**: Card internal padding (small)
 * - **standard (16dp)**: Base screen margin, card spacing
 * - **large (24dp)**: Section spacing
 * - **extraLarge (32dp)**: Major section spacing
 * - **huge (48dp)**: Empty states, hero sections
 *
 * ## Semantic Tokens
 *
 * - **screenHorizontal**: Left/right screen margins (responsive)
 * - **cardPadding**: Internal padding for cards (responsive)
 * - **itemSpacing**: Spacing between list items
 * - **sectionSpacing**: Spacing between major sections
 *
 * @see createResponsiveSpacing for screen size adaptive spacing
 */
data class AppSpacing(
    // Base spacing scale (8dp grid)
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 12.dp,
    val standard: Dp = 16.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 32.dp,
    val huge: Dp = 48.dp,

    // Semantic spacing tokens (responsive)
    val screenHorizontal: Dp = 16.dp,
    val cardPadding: Dp = 16.dp,
    val itemSpacing: Dp = 8.dp,
    val sectionSpacing: Dp = 24.dp
)

/**
 * CompositionLocal for accessing spacing tokens throughout the app.
 *
 * Access via: `val spacing = LocalSpacing.current`
 */
val LocalSpacing = compositionLocalOf { AppSpacing() }

/**
 * Creates responsive spacing based on screen width in Dp.
 *
 * This function is KMP-compatible and uses screen width measurements
 * from BoxWithConstraints or similar composables.
 *
 * Breakpoints (Material Design Window Size Classes):
 * - **Compact** (<360dp): Small phones - 12dp margins
 * - **Medium** (360-599dp): Normal phones - 16dp margins (default)
 * - **Expanded** (600-839dp): Large phones/Small tablets - 20dp margins
 * - **Large** (840dp+): Tablets - 24dp margins
 *
 * @param screenWidthDp The screen width in Dp units
 * @return AppSpacing configured for the given screen width
 */
fun createResponsiveSpacing(screenWidthDp: Dp): AppSpacing {
    return when {
        // Small phones (<360dp): Compact spacing
        screenWidthDp < 360.dp -> AppSpacing(
            screenHorizontal = 12.dp,
            cardPadding = 12.dp,
            sectionSpacing = 20.dp
        )

        // Normal phones (360-599dp): Default spacing
        screenWidthDp < 600.dp -> AppSpacing(
            screenHorizontal = 16.dp,
            cardPadding = 16.dp,
            sectionSpacing = 24.dp
        )

        // Large phones/Small tablets (600-839dp): Expanded spacing
        screenWidthDp < 840.dp -> AppSpacing(
            screenHorizontal = 20.dp,
            cardPadding = 20.dp,
            sectionSpacing = 28.dp
        )

        // Tablets (840dp+): Large spacing
        else -> AppSpacing(
            screenHorizontal = 24.dp,
            cardPadding = 24.dp,
            sectionSpacing = 32.dp,
            extraLarge = 40.dp,
            huge = 56.dp
        )
    }
}

/**
 * Extension properties for common spacing patterns.
 *
 * These provide convenient shortcuts for frequently used spacing combinations.
 */

/**
 * Standard vertical spacing between items in a list.
 * Equivalent to: `Modifier.padding(vertical = spacing.itemSpacing)`
 */
val AppSpacing.verticalItemSpacing: Dp
    get() = itemSpacing

/**
 * Standard horizontal spacing for full-width content.
 * Equivalent to: `Modifier.padding(horizontal = spacing.screenHorizontal)`
 */
val AppSpacing.horizontalScreenPadding: Dp
    get() = screenHorizontal
