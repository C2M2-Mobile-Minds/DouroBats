package pt.dourobats.app.features.schedule.api

import kotlinx.datetime.Instant

/**
 * Represents a user's booking for a training session.
 *
 * @property id Unique identifier for the booking.
 * @property sessionId ID of the booked [Session].
 * @property userId ID of the user who made the booking.
 * @property confirmedAt Timestamp of when the booking was confirmed. Null if still [BookingStatus.PENDING].
 * @property status Current lifecycle state of the booking.
 */
data class Booking(
    val id: String,
    val sessionId: String,
    val userId: String,
    val confirmedAt: Instant? = null,
    val status: BookingStatus = BookingStatus.PENDING
)
