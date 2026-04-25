package pt.dourobats.app.features.venues.testing

import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.venues.api.model.Venue
import pt.dourobats.app.features.venues.api.usecase.GetVenueByIdUseCase

class FakeGetVenueByIdUseCase : GetVenueByIdUseCase {
    var result: Result<Venue> = Result.Success(
        Venue(id = "default", name = "Default Venue", address = "", capacity = 0, sportIds = emptyList())
    )
    var lastId: String? = null
    var resultProvider: ((String) -> Result<Venue>)? = null

    override suspend fun invoke(id: String): Result<Venue> {
        lastId = id
        return resultProvider?.invoke(id) ?: result
    }
}
