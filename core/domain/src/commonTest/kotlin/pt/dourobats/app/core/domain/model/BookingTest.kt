package pt.dourobats.app.core.domain.model

import pt.dourobats.app.features.schedule.api.model.Booking
import pt.dourobats.app.features.schedule.api.model.BookingStatus
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(kotlin.experimental.ExperimentalNativeApi::class)
class BookingTest {

    @Test
    fun `booking defaults to pending status and no confirmation time`() {
        val booking = Booking(id = "b1", sessionId = "s1", userId = "u1")

        assertEquals(BookingStatus.PENDING, booking.status)
        assertNull(booking.confirmedAt)
    }

    @Test
    fun `booking is created with confirmed status and timestamp`() {
        val confirmedAt = Instant.fromEpochSeconds(1_700_000_000)
        val booking = Booking(
            id = "b2",
            sessionId = "s2",
            userId = "u2",
            confirmedAt = confirmedAt,
            status = BookingStatus.CONFIRMED
        )

        assertEquals("b2", booking.id)
        assertEquals("s2", booking.sessionId)
        assertEquals("u2", booking.userId)
        assertEquals(confirmedAt, booking.confirmedAt)
        assertEquals(BookingStatus.CONFIRMED, booking.status)
    }

    @Test
    fun `booking can be cancelled`() {
        val booking = Booking(id = "b3", sessionId = "s3", userId = "u3", status = BookingStatus.CANCELLED)

        assertEquals(BookingStatus.CANCELLED, booking.status)
    }

    @Test
    fun `copy updates status`() {
        val booking = Booking(id = "b4", sessionId = "s4", userId = "u4")
        val confirmed = booking.copy(
            status = BookingStatus.CONFIRMED,
            confirmedAt = Instant.fromEpochSeconds(1_700_000_000)
        )

        assertEquals(BookingStatus.CONFIRMED, confirmed.status)
        assertEquals(booking.id, confirmed.id)
    }
}
