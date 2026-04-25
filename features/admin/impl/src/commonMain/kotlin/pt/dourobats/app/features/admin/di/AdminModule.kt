package pt.dourobats.app.features.admin.di

import org.koin.dsl.module
import pt.dourobats.app.features.admin.api.repository.AdminRepository
import pt.dourobats.app.features.admin.api.usecase.*
import pt.dourobats.app.features.admin.data.FakeAdminRepository
import pt.dourobats.app.features.admin.usecase.*

val adminModule = module {
    single<AdminRepository> { FakeAdminRepository() }

    factory<UnlockCalendarUseCase> { UnlockCalendarUseCaseImpl(get()) }
    factory<GetUnlockHistoryUseCase> { GetUnlockHistoryUseCaseImpl(get()) }
    factory<UpdateAthleteLevelUseCase> { UpdateAthleteLevelUseCaseImpl(get()) }
}
