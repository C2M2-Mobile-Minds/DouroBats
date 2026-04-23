package pt.dourobats.app.core.ui.localization

import android.os.LocaleList
import pt.dourobats.app.features.settings.api.model.Language
import java.util.Locale

actual fun changeLanguage(language: Language) {
    val locale = Locale.forLanguageTag(language.bcp47Tag)
    Locale.setDefault(locale)
    LocaleList.setDefault(LocaleList(locale))
}
