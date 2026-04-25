package pt.dourobats.app.features.admin.api.usecase

import kotlinx.datetime.LocalDate
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.admin.api.model.CalendarUnlock

/**
 * Use case for unlocking a calendar period for a specific sport.
 */
interface UnlockCalendarUseCase {
    /**
     * Unlocks a calendar period.
     *
     * @param sportId The sport to unlock the calendar for
     * @param startDate The start date of the period
     * @param endDate The end date of the period
     * @return Result containing the unlock record or an error
     */
    suspend operator fun invoke(sportId: String, startDate: LocalDate, endDate: LocalDate): Result<CalendarUnlock>
}
