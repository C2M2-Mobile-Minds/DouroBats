package pt.dourobats.app.features.settings.api.model

import kotlin.test.Test
import kotlin.test.assertEquals

class ThemeTest {
    @Test
    fun `fromValue returns correct theme for valid input`() {
        assertEquals(Theme.LIGHT, Theme.fromValue("LIGHT"))
        assertEquals(Theme.DARK, Theme.fromValue("DARK"))
    }

    @Test
    fun `fromValue returns LIGHT for unknown value`() {
        assertEquals(Theme.LIGHT, Theme.fromValue("UNKNOWN"))
    }
}
