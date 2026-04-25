package pt.dourobats.app.features.venues.api.usecase

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.venues.api.model.Venue

/**
 * Use case for retrieving a single venue by its ID.
 */
interface GetVenueByIdUseCase {
    /**
     * Get a venue by ID.
     *
     * @param id The unique identifier of the venue
     * @return Result containing the venue or an error
     */
    suspend operator fun invoke(id: String): Result<Venue>
}
