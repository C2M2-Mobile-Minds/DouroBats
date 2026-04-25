package pt.dourobats.app.features.admin.api.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

/**
 * Represents a record of a calendar period being unlocked for a specific sport.
 * 
 * @param id Unique identifier for this unlock record
 * @param sportId The sport this unlock applies to
 * @param startDate The start date of the unlocked period
 * @param endDate The end date of the unlocked period
 * @param unlockedBy User ID of the committee member who performed the unlock
 * @param unlockedAt Timestamp when the unlock occurred
 */
data class CalendarUnlock(
    val id: String,
    val sportId: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val unlockedBy: String,
    val unlockedAt: Instant
)
