package pt.dourobats.app.core.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(kotlin.experimental.ExperimentalNativeApi::class)
class VenueTest {

    @Test
    fun `venue is created with all fields`() {
        val sport = Sport(id = "volleyball", name = "Volleyball", description = "", iconResource = "🏐")
        val venue = Venue(
            id = "v1",
            name = "Campo Principal",
            address = "Rua do Campo, 1, Porto",
            capacity = 50,
            supportedSports = listOf(sport)
        )

        assertEquals("v1", venue.id)
        assertEquals("Campo Principal", venue.name)
        assertEquals("Rua do Campo, 1, Porto", venue.address)
        assertEquals(50, venue.capacity)
        assertEquals(1, venue.supportedSports.size)
        assertEquals("volleyball", venue.supportedSports.first().id)
    }

    @Test
    fun `venue defaults to empty supported sports`() {
        val venue = Venue(id = "v2", name = "Campo 2", address = "Rua B, Porto", capacity = 20)

        assertTrue(venue.supportedSports.isEmpty())
    }

    @Test
    fun `venue supports multiple sports`() {
        val sports = listOf(
            Sport(id = "volleyball", name = "Volleyball", description = "", iconResource = "🏐"),
            Sport(id = "futsal", name = "Futsal", description = "", iconResource = "⚽")
        )
        val venue = Venue(id = "v3", name = "Pavilhão", address = "Rua C, Porto", capacity = 100, supportedSports = sports)

        assertEquals(2, venue.supportedSports.size)
    }
}
