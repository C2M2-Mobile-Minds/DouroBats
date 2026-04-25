package pt.dourobats.app.features.venues.usecase

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.features.venues.api.model.Venue
import pt.dourobats.app.features.venues.api.repository.VenueRepository
import pt.dourobats.app.features.venues.api.usecase.GetVenuesUseCase

internal class GetVenuesUseCaseImpl(
    private val venueRepository: VenueRepository
) : GetVenuesUseCase {
    override fun invoke(sportId: String?): Flow<List<Venue>> {
        return if (sportId != null) {
            venueRepository.getVenuesBySport(sportId)
        } else {
            venueRepository.getVenues()
        }
    }
}
