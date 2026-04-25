package pt.dourobats.app.features.admin.usecase

import kotlinx.datetime.LocalDate
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.admin.api.model.CalendarUnlock
import pt.dourobats.app.features.admin.api.repository.AdminRepository
import pt.dourobats.app.features.admin.api.usecase.UnlockCalendarUseCase

internal class UnlockCalendarUseCaseImpl(
    private val adminRepository: AdminRepository
) : UnlockCalendarUseCase {
    override suspend fun invoke(sportId: String, startDate: LocalDate, endDate: LocalDate): Result<CalendarUnlock> {
        // Business Rule: sequential unlocking and 3-month limit would be checked here or in the repository
        return adminRepository.unlockCalendar(sportId, startDate, endDate)
    }
}
