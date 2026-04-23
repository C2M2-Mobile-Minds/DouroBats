package pt.dourobats.app.core.domain.usecase

import pt.dourobats.app.features.login.api.model.UserProfile

/**
 * Use case for saving an updated user profile.
 */
interface UpdateUserProfileUseCase {
    suspend operator fun invoke(profile: UserProfile)
}
