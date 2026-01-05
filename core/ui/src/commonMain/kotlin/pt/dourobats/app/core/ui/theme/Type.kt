package pt.dourobats.app.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dourobats.core.ui.generated.resources.Res
import dourobats.core.ui.generated.resources.quicksand_bold
import dourobats.core.ui.generated.resources.quicksand_medium
import dourobats.core.ui.generated.resources.quicksand_regular
import dourobats.core.ui.generated.resources.quicksand_semibold
import org.jetbrains.compose.resources.Font

/**
 * Poppins font family loaded from Compose Resources (KMP-compatible).
 *
 * Poppins is a modern geometric sans-serif typeface designed for:
 * - Excellent readability on screens
 * - Clean, professional appearance
 * - Modern, minimalist aesthetics
 *
 * Font weights available:
 * - Regular (400)
 * - Medium (500)
 * - SemiBold (600)
 * - Bold (700)
 *
 * Note: Font loading from resources requires @Composable context
 */
@Composable
fun quicksandFontFamily() = FontFamily(
    Font(Res.font.quicksand_regular, FontWeight.Normal),
    Font(Res.font.quicksand_medium, FontWeight.Medium),
    Font(Res.font.quicksand_semibold, FontWeight.SemiBold),
    Font(Res.font.quicksand_bold, FontWeight.Bold),
)

/**
 * App Typography using Poppins font family.
 *
 * Applies Poppins to all Material Design 3 text styles:
 * - Display styles: Large, prominent text (e.g., hero headlines)
 * - Headline styles: Section headers
 * - Title styles: Card titles, list headers
 * - Body styles: Main content text
 * - Label styles: Buttons, captions, small text
 *
 * @return Typography configured with Poppins font family
 */
@Composable
fun appTypography(): Typography {
    val fontFamily = quicksandFontFamily()
    val baseline = Typography()

    return Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = fontFamily),
        displayMedium = baseline.displayMedium.copy(fontFamily = fontFamily),
        displaySmall = baseline.displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = fontFamily),
        titleMedium = baseline.titleMedium.copy(fontFamily = fontFamily),
        titleSmall = baseline.titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = baseline.bodySmall.copy(fontFamily = fontFamily),
        labelLarge = baseline.labelLarge.copy(fontFamily = fontFamily),
        labelMedium = baseline.labelMedium.copy(fontFamily = fontFamily),
        labelSmall = baseline.labelSmall.copy(fontFamily = fontFamily),
    )
}

