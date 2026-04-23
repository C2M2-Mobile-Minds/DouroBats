package pt.dourobats.app.core.localization

import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults

actual fun changeLanguage(language: Language) {
    NSUserDefaults.standardUserDefaults.setObject(
        listOf(language.bcp47Tag),
        forKey = "AppleLanguages"
    )
    NSUserDefaults.standardUserDefaults.synchronize()
}

actual fun getSystemLocaleLanguage(): Language {
    val tag = NSLocale.preferredLanguages.firstOrNull() as? String ?: "en"
    return Language.fromCodeOrNull(tag)
        ?: Language.fromCodeOrNull(tag.substringBefore("-"))
        ?: Language.ENGLISH_US
}
