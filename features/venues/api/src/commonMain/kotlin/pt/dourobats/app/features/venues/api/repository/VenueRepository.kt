package pt.dourobats.app.features.venues.api.repository

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.venues.api.model.Venue

/**
 * Repository interface for managing [Venue] data.
 */
interface VenueRepository {
    /**
     * Returns a stream of all available venues.
     */
    fun getVenues(): Flow<List<Venue>>

    /**
     * Returns a stream of venues that support the given sport.
     *
     * @param sportId The unique identifier of the sport
     */
    fun getVenuesBySport(sportId: String): Flow<List<Venue>>

    /**
     * Returns a venue by its unique identifier.
     *
     * @param id The unique identifier of the venue
     */
    suspend fun getVenueById(id: String): Result<Venue>
}
