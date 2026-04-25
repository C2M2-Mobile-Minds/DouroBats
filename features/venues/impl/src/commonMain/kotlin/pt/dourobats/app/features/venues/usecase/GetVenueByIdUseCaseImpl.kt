package pt.dourobats.app.features.venues.usecase

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.venues.api.model.Venue
import pt.dourobats.app.features.venues.api.repository.VenueRepository
import pt.dourobats.app.features.venues.api.usecase.GetVenueByIdUseCase

internal class GetVenueByIdUseCaseImpl(
    private val venueRepository: VenueRepository
) : GetVenueByIdUseCase {
    override suspend fun invoke(id: String): Result<Venue> {
        return venueRepository.getVenueById(id)
    }
}
