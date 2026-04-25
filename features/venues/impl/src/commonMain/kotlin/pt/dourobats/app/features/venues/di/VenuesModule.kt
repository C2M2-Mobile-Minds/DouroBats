package pt.dourobats.app.features.venues.di

import org.koin.dsl.module
import pt.dourobats.app.features.venues.api.repository.VenueRepository
import pt.dourobats.app.features.venues.api.usecase.GetVenueByIdUseCase
import pt.dourobats.app.features.venues.api.usecase.GetVenuesUseCase
import pt.dourobats.app.features.venues.data.FakeVenueRepository
import pt.dourobats.app.features.venues.usecase.GetVenueByIdUseCaseImpl
import pt.dourobats.app.features.venues.usecase.GetVenuesUseCaseImpl

val venuesModule = module {
    single<VenueRepository> { FakeVenueRepository() }

    factory<GetVenuesUseCase> { GetVenuesUseCaseImpl(get()) }
    factory<GetVenueByIdUseCase> { GetVenueByIdUseCaseImpl(get()) }
}
