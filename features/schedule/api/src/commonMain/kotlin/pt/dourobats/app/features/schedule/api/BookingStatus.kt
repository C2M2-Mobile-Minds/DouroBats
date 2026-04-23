package pt.dourobats.app.features.schedule.api

/**
 * Represents the lifecycle state of a session booking.
 *
 * - [PENDING] – booking request submitted but not yet confirmed.
 * - [CONFIRMED] – booking has been confirmed.
 * - [CANCELLED] – booking was cancelled by the user or the system.
 */
enum class BookingStatus {
    PENDING,
    CONFIRMED,
    CANCELLED
}
