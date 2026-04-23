package pt.dourobats.app.core.ui.localization

import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import pt.dourobats.app.features.settings.api.model.Language

actual fun changeLanguage(language: Language) {
    NSUserDefaults.standardUserDefaults.setObject(
        listOf(language.bcp47Tag),
        forKey = "AppleLanguages"
    )
    NSUserDefaults.standardUserDefaults.synchronize()
}

actual fun getSystemLocaleLanguage(): Language {
    // preferredLanguages gives full BCP 47 tags e.g. "pt-PT", "en-US"
    val tag = NSLocale.preferredLanguages.firstOrNull() as? String ?: "en"
    return Language.fromCodeOrNull(tag)
        ?: Language.fromCodeOrNull(tag.substringBefore("-"))
        ?: Language.ENGLISH_US
}
