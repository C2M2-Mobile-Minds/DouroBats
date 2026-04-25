package pt.dourobats.app.features.venues.testing

import kotlinx.coroutines.flow.Flow
import pt.dourobats.app.features.venues.api.model.Venue
import pt.dourobats.app.features.venues.api.usecase.GetVenuesUseCase

fun fakeGetVenuesUseCase(builder: FakeGetVenuesUseCase.() -> Unit = {}): GetVenuesUseCase =
    FakeGetVenuesUseCase().apply(builder).build()

class FakeGetVenuesUseCase {
    var invoke: (sportId: String?) -> Flow<List<Venue>> =
        { _ -> throw NotImplementedError() }

    fun build(): GetVenuesUseCase =
        object : GetVenuesUseCase {
            override fun invoke(sportId: String?): Flow<List<Venue>> =
                this@FakeGetVenuesUseCase.invoke(sportId)
        }
}
