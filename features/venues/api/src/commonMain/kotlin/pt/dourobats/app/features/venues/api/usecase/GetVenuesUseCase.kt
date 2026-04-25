package pt.dourobats.app.features.venues.api.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.features.venues.api.model.Venue

/**
 * Use case for retrieving all available training venues.
 */
interface GetVenuesUseCase {
    /**
     * Get all venues.
     *
     * @param sportId Optional sport ID to filter venues by
     * @return Flow of list of venues
     */
    operator fun invoke(sportId: String? = null): Flow<List<Venue>>
}
