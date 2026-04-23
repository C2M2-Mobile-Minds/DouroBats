package pt.dourobats.app.features.schedule.di

import org.koin.core.module.dsl.*
import org.koin.dsl.module
import pt.dourobats.app.core.domain.di.domainModule
import pt.dourobats.app.features.schedule.ScheduleViewModel
import pt.dourobats.app.features.schedule.api.TrainingRepository
import pt.dourobats.app.features.schedule.data.FakeTrainingRepository

val scheduleModule = module {
    includes(domainModule)

    single<TrainingRepository> { FakeTrainingRepository() }

    // ViewModel
    viewModel {
        ScheduleViewModel(
            getAvailableSessionsUseCase = get(),
            getUserBookedSessionsUseCase = get(),
            getAllSessionsUseCase = get(),
            bookSessionUseCase = get(),
            cancelBookingUseCase = get()
        )
    }
}
