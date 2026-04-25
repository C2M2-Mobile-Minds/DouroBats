package pt.dourobats.app.features.admin.usecase

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.admin.api.repository.AdminRepository
import pt.dourobats.app.features.admin.api.usecase.UpdateAthleteLevelUseCase

internal class UpdateAthleteLevelUseCaseImpl(
    private val adminRepository: AdminRepository
) : UpdateAthleteLevelUseCase {
    override suspend fun invoke(athleteId: String, sportId: String, newLevel: String, reason: String): Result<Unit> {
        return adminRepository.updateAthleteLevel(athleteId, sportId, newLevel, reason)
    }
}
