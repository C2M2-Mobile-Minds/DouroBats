package pt.dourobats.app.features.schedule.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pt.dourobats.app.core.domain.usecase.GetAvailableSessionsUseCase
import pt.dourobats.app.core.domain.usecase.GetUserBookedSessionsUseCase
import pt.dourobats.app.features.schedule.ScheduleViewModel

val scheduleModule = module {
    // Use cases
    factory { GetAvailableSessionsUseCase(get()) }
    factory { GetUserBookedSessionsUseCase(get()) }

    // ViewModel
    viewModel {
        ScheduleViewModel(
            getAvailableSessionsUseCase = get(),
            getUserBookedSessionsUseCase = get()
        )
    }
}
