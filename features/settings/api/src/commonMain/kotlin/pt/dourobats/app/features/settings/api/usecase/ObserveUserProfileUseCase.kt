package pt.dourobats.app.features.settings.api.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.features.login.api.model.UserProfile

/**
 * Use case for observing the current user profile as a continuous stream.
 */
interface ObserveUserProfileUseCase {
    operator fun invoke(): Flow<UserProfile>
}
