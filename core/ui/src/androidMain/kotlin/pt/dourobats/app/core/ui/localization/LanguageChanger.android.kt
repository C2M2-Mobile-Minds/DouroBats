package pt.dourobats.app.core.ui.localization

import pt.dourobats.app.core.model.Language
import java.util.Locale

actual fun changeLanguage(language: Language) {
    val locale = Locale.forLanguageTag(language.bcp47Tag)
    Locale.setDefault(locale)
    println("Changed default locale to: ${Locale.getDefault()}")
}
