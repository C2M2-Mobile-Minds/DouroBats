package pt.dourobats.app.core.ui.localization

import platform.Foundation.NSUserDefaults
import pt.dourobats.app.features.settings.api.model.Language

actual fun changeLanguage(language: Language) {
    // On iOS, update the AppleLanguages preference
    NSUserDefaults.standardUserDefaults.setObject(
        listOf(language.bcp47Tag),
        forKey = "AppleLanguages"
    )
    NSUserDefaults.standardUserDefaults.synchronize()
    println("Changed iOS language preference to: ${language.bcp47Tag}")
}
