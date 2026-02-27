package pt.dourobats.app.features.schedule.di

import org.koin.core.module.dsl.*
import org.koin.dsl.module
import pt.dourobats.app.core.domain.di.domainModule
import pt.dourobats.app.features.schedule.ScheduleViewModel

val scheduleModule = module {
    includes(domainModule)


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
