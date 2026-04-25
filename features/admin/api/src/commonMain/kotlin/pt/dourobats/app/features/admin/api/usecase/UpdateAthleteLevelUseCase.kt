package pt.dourobats.app.features.admin.api.usecase

import pt.dourobats.app.core.common.Result

/**
 * Use case for updating an athlete's skill level.
 */
interface UpdateAthleteLevelUseCase {
    /**
     * Updates the skill level of an athlete for a specific sport.
     * 
     * @param athleteId The ID of the athlete
     * @param sportId The ID of the sport
     * @param newLevel The new skill level (Beginner, Intermediate, Advanced)
     * @param reason The reason for the update
     */
    suspend operator fun invoke(athleteId: String, sportId: String, newLevel: String, reason: String): Result<Unit>
}
