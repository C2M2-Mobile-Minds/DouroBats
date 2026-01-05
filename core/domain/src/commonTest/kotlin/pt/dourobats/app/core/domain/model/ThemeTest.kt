package pt.dourobats.app.core.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(kotlin.experimental.ExperimentalNativeApi::class)
class ThemeTest {

    @Test
    fun `fromValue is LIGHT when LIGHT provided`() {
        // Arrange & Act
        val theme = Theme.fromValue("LIGHT")

        // Assert
        assertEquals(Theme.LIGHT, theme)
    }

    @Test
    fun `fromValue is DARK when DARK provided`() {
        // Arrange & Act
        val theme = Theme.fromValue("DARK")

        // Assert
        assertEquals(Theme.DARK, theme)
    }

    @Test
    fun `fromValue is LIGHT when invalid value provided`() {
        // Arrange & Act
        val theme = Theme.fromValue("INVALID")

        // Assert
        assertEquals(Theme.LIGHT, theme)
    }

    @Test
    fun `fromValue is LIGHT when empty string provided`() {
        // Arrange & Act
        val theme = Theme.fromValue("")

        // Assert
        assertEquals(Theme.LIGHT, theme)
    }

    @Test
    fun `all themes have display names`() {
        // Arrange & Act & Assert
        Theme.entries.forEach { theme ->
            assert(theme.displayName.isNotEmpty()) {
                "Theme ${theme.name} should have a display name"
            }
        }
    }

    @Test
    fun `LIGHT has correct display name`() {
        // Arrange
        val theme = Theme.LIGHT

        // Act & Assert
        assertEquals("Light", theme.displayName)
    }

    @Test
    fun `DARK has correct display name`() {
        // Arrange
        val theme = Theme.DARK

        // Act & Assert
        assertEquals("Dark", theme.displayName)
    }
}
