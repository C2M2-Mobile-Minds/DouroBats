package pt.dourobats.app.core.ui.localization

import androidx.compose.runtime.staticCompositionLocalOf
import pt.dourobats.app.core.model.Language

/**
 * CompositionLocal for the current language.
 * When this changes, all composables reading from it will recompose.
 */
val LocalLanguage = staticCompositionLocalOf { Language.ENGLISH_US }

/**
 * Platform-specific function to change the app's locale.
 * This updates the system's default locale so that stringResource() picks up the new language.
 */
expect fun changeLanguage(language: Language)
