package pt.dourobats.app.core.domain.model

import pt.dourobats.app.features.settings.api.Language
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(kotlin.experimental.ExperimentalNativeApi::class)
class LanguageTest {

    @Test
    fun `code is not empty when language is accessed`() {
        // Arrange & Act
        val languages = Language.entries

        // Assert
        languages.forEach { language ->
            assertNotNull(language.code, "Language ${language.name} should have a code")
            assert(language.code.isNotEmpty()) { "Language ${language.name} code should not be empty" }
        }
    }

    @Test
    fun `bcp47Tag is not empty when language is accessed`() {
        // Arrange & Act
        val languages = Language.entries

        // Assert
        languages.forEach { language ->
            assertNotNull(language.bcp47Tag, "Language ${language.name} should have a BCP 47 tag")
            assert(language.bcp47Tag.isNotEmpty()) { "Language ${language.name} BCP 47 tag should not be empty" }
        }
    }

    @Test
    fun `resourceQualifier is not empty when language is accessed`() {
        // Arrange & Act
        val languages = Language.entries

        // Assert
        languages.forEach { language ->
            assertNotNull(language.resourceQualifier, "Language ${language.name} should have a resource qualifier")
            assert(language.resourceQualifier.isNotEmpty()) { "Language ${language.name} resource qualifier should not be empty" }
        }
    }

    @Test
    fun `displayName is not empty when language is accessed`() {
        // Arrange & Act
        val languages = Language.entries

        // Assert
        languages.forEach { language ->
            assertNotNull(language.displayName, "Language ${language.name} should have a display name")
            assert(language.displayName.isNotEmpty()) { "Language ${language.name} display name should not be empty" }
        }
    }

    @Test
    fun `fromCode is correct language when valid code provided`() {
        // Arrange & Act & Assert
        assertEquals(Language.ENGLISH_US, Language.fromCode("en"))
        assertEquals(Language.ENGLISH_GB, Language.fromCode("en-GB"))
        assertEquals(Language.PORTUGUESE_BR, Language.fromCode("pt-BR"))
        assertEquals(Language.PORTUGUESE_PT, Language.fromCode("pt-PT"))
        assertEquals(Language.SPANISH, Language.fromCode("es"))
    }

    @Test
    fun `fromCode is ENGLISH_US when invalid code provided`() {
        // Arrange & Act & Assert
        assertEquals(Language.ENGLISH_US, Language.fromCode("invalid"))
        assertEquals(Language.ENGLISH_US, Language.fromCode(""))
        assertEquals(Language.ENGLISH_US, Language.fromCode("xyz"))
    }

    @Test
    fun `fromCode is ENGLISH_US when uppercase code provided`() {
        // Arrange & Act & Assert
        assertEquals(Language.ENGLISH_US, Language.fromCode("EN"))
        assertEquals(Language.ENGLISH_US, Language.fromCode("En"))
    }

    @Test
    fun `getSystemDefault is valid language when called`() {
        // Arrange & Act
        val systemDefault = Language.getSystemDefault()

        // Assert
        assertNotNull(systemDefault)
        assert(Language.entries.contains(systemDefault)) {
            "System default should be one of the supported languages"
        }
    }

    @Test
    fun `ENGLISH_US is correct properties when accessed`() {
        // Arrange
        val language = Language.ENGLISH_US

        // Act & Assert
        assertEquals("en", language.code)
        assertEquals("en", language.resourceQualifier)
        assertEquals("en-US", language.bcp47Tag)
        assertEquals("English (US)", language.displayName)
    }

    @Test
    fun `ENGLISH_GB is correct properties when accessed`() {
        // Arrange
        val language = Language.ENGLISH_GB

        // Act & Assert
        assertEquals("en-GB", language.code)
        assertEquals("en-rGB", language.resourceQualifier)
        assertEquals("en-GB", language.bcp47Tag)
        assertEquals("English (UK)", language.displayName)
    }

    @Test
    fun `PORTUGUESE_BR is correct properties when accessed`() {
        // Arrange
        val language = Language.PORTUGUESE_BR

        // Act & Assert
        assertEquals("pt-BR", language.code)
        assertEquals("pt-rBR", language.resourceQualifier)
        assertEquals("pt-BR", language.bcp47Tag)
        assertEquals("Português (Brasil)", language.displayName)
    }

    @Test
    fun `PORTUGUESE_PT is correct properties when accessed`() {
        // Arrange
        val language = Language.PORTUGUESE_PT

        // Act & Assert
        assertEquals("pt-PT", language.code)
        assertEquals("pt-rPT", language.resourceQualifier)
        assertEquals("pt-PT", language.bcp47Tag)
        assertEquals("Português (Portugal)", language.displayName)
    }

    @Test
    fun `SPANISH is correct properties when accessed`() {
        // Arrange
        val language = Language.SPANISH

        // Act & Assert
        assertEquals("es", language.code)
        assertEquals("es-rES", language.resourceQualifier)
        assertEquals("es-ES", language.bcp47Tag)
        assertEquals("Español", language.displayName)
    }

    @Test
    fun `localeTag is bcp47Tag when deprecated property accessed`() {
        // Arrange
        val usLanguage = Language.ENGLISH_US
        val brLanguage = Language.PORTUGUESE_BR

        // Act & Assert
        @Suppress("DEPRECATION")
        assertEquals(usLanguage.bcp47Tag, usLanguage.localeTag)
        @Suppress("DEPRECATION")
        assertEquals(brLanguage.bcp47Tag, brLanguage.localeTag)
    }

    @Test
    fun `codes are unique when all languages compared`() {
        // Arrange & Act
        val codes = Language.entries.map { it.code }
        val uniqueCodes = codes.distinct()

        // Assert
        assertEquals(codes.size, uniqueCodes.size, "All language codes should be unique")
    }

    @Test
    fun `bcp47Tags are unique when all languages compared`() {
        // Arrange & Act
        val tags = Language.entries.map { it.bcp47Tag }
        val uniqueTags = tags.distinct()

        // Assert
        assertEquals(tags.size, uniqueTags.size, "All BCP 47 tags should be unique")
    }

    @Test
    fun `entries is 5 languages when all languages counted`() {
        // Arrange & Act
        val count = Language.entries.size

        // Assert
        assertEquals(5, count)
    }
}
