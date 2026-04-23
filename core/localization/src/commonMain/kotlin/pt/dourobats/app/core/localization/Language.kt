package pt.dourobats.app.core.localization

enum class Language(
    val code: String,
    val resourceQualifier: String,
    val bcp47Tag: String,
    val displayName: String
) {
    ENGLISH_US(code = "en", resourceQualifier = "en", bcp47Tag = "en-US", displayName = "English (US)"),
    ENGLISH_GB(code = "en-GB", resourceQualifier = "en-rGB", bcp47Tag = "en-GB", displayName = "English (UK)"),
    PORTUGUESE_BR(code = "pt-BR", resourceQualifier = "pt-rBR", bcp47Tag = "pt-BR", displayName = "Português (Brasil)"),
    PORTUGUESE_PT(code = "pt-PT", resourceQualifier = "pt-rPT", bcp47Tag = "pt-PT", displayName = "Português (Portugal)"),
    SPANISH(code = "es", resourceQualifier = "es-rES", bcp47Tag = "es-ES", displayName = "Español");

    @Deprecated("Use bcp47Tag for Locale API or resourceQualifier for Android resources", ReplaceWith("bcp47Tag"))
    val localeTag: String get() = bcp47Tag

    companion object {
        fun fromCode(code: String): Language = entries.find { it.code == code } ?: ENGLISH_US
        fun fromCodeOrNull(code: String): Language? =
            entries.find { it.code.equals(code, ignoreCase = true) }
                ?: entries.find { code.startsWith(it.code, ignoreCase = true) }
        fun getSystemDefault(): Language = ENGLISH_US
    }
}
