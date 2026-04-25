package pt.dourobats.app.features.venues.testing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import pt.dourobats.app.features.venues.api.model.Venue
import pt.dourobats.app.features.venues.api.usecase.GetVenuesUseCase

class FakeGetVenuesUseCase : GetVenuesUseCase {
    var result: Flow<List<Venue>> = flowOf(emptyList())
    var lastSportId: String? = null

    override fun invoke(sportId: String?): Flow<List<Venue>> {
        lastSportId = sportId
        return result
    }
}
