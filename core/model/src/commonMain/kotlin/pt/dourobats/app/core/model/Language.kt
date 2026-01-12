package pt.dourobats.app.core.domain.model

/**
 * Represents supported languages in the application.
 * Each language has:
 * - code: Persistence identifier (BCP 47 format)
 * - resourceQualifier: Android resource qualifier format (e.g., "en-rGB")
 * - bcp47Tag: BCP 47 language tag for Locale API (e.g., "en-GB")
 * - displayName: Human-readable name
 */
enum class Language(
    val code: String,
    val resourceQualifier: String,
    val bcp47Tag: String,
    val displayName: String
) {
    ENGLISH_US(
        code = "en",
        resourceQualifier = "en",
        bcp47Tag = "en-US",
        displayName = "English (US)"
    ),
    ENGLISH_GB(
        code = "en-GB",
        resourceQualifier = "en-rGB",
        bcp47Tag = "en-GB",
        displayName = "English (UK)"
    ),
    PORTUGUESE_BR(
        code = "pt-BR",
        resourceQualifier = "pt-rBR",
        bcp47Tag = "pt-BR",
        displayName = "Português (Brasil)"
    ),
    PORTUGUESE_PT(
        code = "pt-PT",
        resourceQualifier = "pt-rPT",
        bcp47Tag = "pt-PT",
        displayName = "Português (Portugal)"
    ),
    SPANISH(
        code = "es",
        resourceQualifier = "es-rES",
        bcp47Tag = "es-ES",
        displayName = "Español"
    );

    @Deprecated("Use bcp47Tag for Locale API or resourceQualifier for Android resources", ReplaceWith("bcp47Tag"))
    val localeTag: String get() = bcp47Tag

    companion object {
        fun fromCode(code: String): Language {
            return entries.find { it.code == code } ?: ENGLISH_US
        }

        fun getSystemDefault(): Language {
            // TODO: Get actual system locale
            // For now, default to English US
            return ENGLISH_US
        }
    }
}
