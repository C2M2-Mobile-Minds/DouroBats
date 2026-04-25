package pt.dourobats.app.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import dourobats.core.ui.generated.resources.Res
import dourobats.core.ui.generated.resources.lexend_bold
import dourobats.core.ui.generated.resources.lexend_medium
import dourobats.core.ui.generated.resources.lexend_regular
import dourobats.core.ui.generated.resources.lexend_semibold
import dourobats.core.ui.generated.resources.manrope_bold
import dourobats.core.ui.generated.resources.manrope_medium
import dourobats.core.ui.generated.resources.manrope_regular
import dourobats.core.ui.generated.resources.manrope_semibold
import org.jetbrains.compose.resources.Font

/**
 * Kinetic Precision Typography System
 *
 * The Editorial Voice: High-contrast pairing to balance technical precision with human warmth
 *
 * LEXEND (Headlines):
 * - Chosen for hyper-legibility and athletic, geometric structure
 * - Used for Display, Headline, and Title styles
 * - Creates "locked in" professional feel with tight letter spacing
 *
 * MANROPE (Body):
 * - Modern sans-serif with organic touch
 * - High x-height ensures readability during high-intensity use
 * - Used for Body and Label styles
 *
 * Design Philosophy:
 * Never use bold for everything. Pair headline-lg (Regular weight) with label-sm (Bold, All Caps)
 * to create sophisticated, rhythmic contrast.
 */

/**
 * Lexend font family for headlines and display text.
 * Provides hyper-legibility and athletic geometric structure.
 *
 * Font weights available:
 * - Regular (400)
 * - Medium (500)
 * - SemiBold (600)
 * - Bold (700)
 */
@Composable
fun lexendFontFamily() = FontFamily(
    Font(Res.font.lexend_regular, FontWeight.Normal),
    Font(Res.font.lexend_medium, FontWeight.Medium),
    Font(Res.font.lexend_semibold, FontWeight.SemiBold),
    Font(Res.font.lexend_bold, FontWeight.Bold),
)

/**
 * Manrope font family for body text and labels.
 * Modern sans-serif with high x-height for excellent readability.
 *
 * Font weights available:
 * - Regular (400)
 * - Medium (500)
 * - SemiBold (600)
 * - Bold (700)
 */
@Composable
fun manropeFontFamily() = FontFamily(
    Font(Res.font.manrope_regular, FontWeight.Normal),
    Font(Res.font.manrope_medium, FontWeight.Medium),
    Font(Res.font.manrope_semibold, FontWeight.SemiBold),
    Font(Res.font.manrope_bold, FontWeight.Bold),
)

/**
 * App Typography using Kinetic Precision font pairing.
 *
 * Font Assignment:
 * - Display styles (Lexend): Large, prominent text with -2% letter spacing for "locked in" feel
 * - Headline styles (Lexend): Section headers with athletic precision
 * - Title styles (Lexend): Card titles and list headers
 * - Body styles (Manrope): Main content text with organic warmth
 * - Label styles (Manrope): Buttons, captions, small text
 *
 * Hierarchy Hack:
 * Use headline-lg (Regular weight) paired with label-sm (Bold weight, All Caps)
 * for sophisticated, rhythmic contrast.
 *
 * @return Typography configured with Lexend (headlines) and Manrope (body)
 */
@Composable
fun appTypography(): Typography {
    val lexend = lexendFontFamily()
    val manrope = manropeFontFamily()
    val baseline = Typography()

    return Typography(
        // Display styles - Lexend with -2% letter spacing for "locked in" athletic feel
        displayLarge = baseline.displayLarge.copy(
            fontFamily = lexend,
            letterSpacing = (-0.02).em // -2% letter spacing as specified
        ),
        displayMedium = baseline.displayMedium.copy(
            fontFamily = lexend,
            letterSpacing = (-0.01).em
        ),
        displaySmall = baseline.displaySmall.copy(
            fontFamily = lexend
        ),

        // Headline styles - Lexend for section headers
        headlineLarge = baseline.headlineLarge.copy(
            fontFamily = lexend
        ),
        headlineMedium = baseline.headlineMedium.copy(
            fontFamily = lexend
        ),
        headlineSmall = baseline.headlineSmall.copy(
            fontFamily = lexend
        ),

        // Title styles - Lexend for card and list headers
        titleLarge = baseline.titleLarge.copy(
            fontFamily = lexend
        ),
        titleMedium = baseline.titleMedium.copy(
            fontFamily = lexend
        ),
        titleSmall = baseline.titleSmall.copy(
            fontFamily = lexend
        ),

        // Body styles - Manrope for main content with organic warmth
        bodyLarge = baseline.bodyLarge.copy(
            fontFamily = manrope
        ),
        bodyMedium = baseline.bodyMedium.copy(
            fontFamily = manrope
        ),
        bodySmall = baseline.bodySmall.copy(
            fontFamily = manrope
        ),

        // Label styles - Manrope for UI elements
        labelLarge = baseline.labelLarge.copy(
            fontFamily = manrope
        ),
        labelMedium = baseline.labelMedium.copy(
            fontFamily = manrope
        ),
        labelSmall = baseline.labelSmall.copy(
            fontFamily = manrope
        ),
    )
}