package pt.dourobats.app.core.localization

import androidx.compose.runtime.staticCompositionLocalOf

val LocalLanguage = staticCompositionLocalOf { Language.ENGLISH_US }

expect fun changeLanguage(language: Language)

/**
 * Returns the best [Language] match for the device's current locale.
 * Falls back to [Language.ENGLISH_US] if no match is found.
 */
expect fun getSystemLocaleLanguage(): Language
