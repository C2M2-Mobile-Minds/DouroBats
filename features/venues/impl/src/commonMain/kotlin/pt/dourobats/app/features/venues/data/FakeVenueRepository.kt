package pt.dourobats.app.features.venues.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import pt.dourobats.app.core.common.Result
import pt.dourobats.app.features.venues.api.model.Venue
import pt.dourobats.app.features.venues.api.repository.VenueRepository

internal class FakeVenueRepository : VenueRepository {

    private val venues = MutableStateFlow(
        listOf(
            Venue(
                id = "pav-municipal",
                name = "Pavilhão Municipal",
                address = "Rua do Desporto, 123, Porto",
                capacity = 100,
                sportIds = listOf("volleyball", "basketball", "futsal")
            ),
            Venue(
                id = "campo-2",
                name = "Campo 2 - Exterior",
                address = "Parque da Cidade, Porto",
                capacity = 50,
                sportIds = listOf("futsal", "running")
            ),
            Venue(
                id = "padel-hub",
                name = "Padel Hub",
                address = "Zona Industrial, Maia",
                capacity = 24,
                sportIds = listOf("padel")
            )
        )
    )

    override fun getVenues(): Flow<List<Venue>> = venues

    override fun getVenuesBySport(sportId: String): Flow<List<Venue>> {
        return venues.map { list ->
            list.filter { it.sportIds.contains(sportId) }
        }
    }

    override suspend fun getVenueById(id: String): Result<Venue> {
        val venue = venues.value.find { it.id == id }
        return if (venue != null) {
            Result.Success(venue)
        } else {
            Result.Error(Exception("Venue not found"), "Venue with id $id not found")
        }
    }
}
