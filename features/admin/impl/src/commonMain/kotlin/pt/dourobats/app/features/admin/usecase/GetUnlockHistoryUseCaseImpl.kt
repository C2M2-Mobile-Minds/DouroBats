package pt.dourobats.app.features.admin.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.features.admin.api.model.CalendarUnlock
import pt.dourobats.app.features.admin.api.repository.AdminRepository
import pt.dourobats.app.features.admin.api.usecase.GetUnlockHistoryUseCase

internal class GetUnlockHistoryUseCaseImpl(
    private val adminRepository: AdminRepository
) : GetUnlockHistoryUseCase {
    override fun invoke(sportId: String): Flow<List<CalendarUnlock>> {
        return adminRepository.getUnlockHistory(sportId)
    }
}
