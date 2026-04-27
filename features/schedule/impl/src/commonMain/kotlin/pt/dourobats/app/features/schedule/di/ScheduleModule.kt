package pt.dourobats.app.features.schedule.di

import org.koin.core.module.dsl.*
import org.koin.core.qualifier.named
import org.koin.dsl.module
import pt.dourobats.app.core.common.isDebug
import pt.dourobats.app.core.navigation.FeatureGraph
import pt.dourobats.app.features.schedule.ScheduleGraph
import pt.dourobats.app.features.schedule.api.usecase.BookSessionUseCase
import pt.dourobats.app.features.schedule.api.usecase.CancelBookingUseCase
import pt.dourobats.app.features.schedule.api.usecase.GetAllSessionsUseCase
import pt.dourobats.app.features.schedule.api.usecase.GetAvailableSessionsUseCase
import pt.dourobats.app.features.schedule.api.usecase.GetUserBookedSessionsUseCase
import pt.dourobats.app.features.schedule.data.FakeTrainingRepository
import pt.dourobats.app.features.schedule.data.TrainingRepositoryImpl
import pt.dourobats.app.features.schedule.ScheduleNavigation
import pt.dourobats.app.features.schedule.navigation.ScheduleNavigationImpl
import pt.dourobats.app.features.schedule.repository.TrainingRepository
import pt.dourobats.app.features.schedule.ui.ScheduleViewModel
import pt.dourobats.app.features.schedule.ui.mapper.SessionUiMapper
import pt.dourobats.app.features.schedule.usecase.BookSessionUseCaseImpl
import pt.dourobats.app.features.schedule.usecase.CancelBookingUseCaseImpl
import pt.dourobats.app.features.schedule.usecase.GetAllSessionsUseCaseImpl
import pt.dourobats.app.features.schedule.usecase.GetAvailableSessionsUseCaseImpl
import pt.dourobats.app.features.schedule.usecase.GetUserBookedSessionsUseCaseImpl

val scheduleModule = module {
    single<FeatureGraph>(named("schedule")) { ScheduleGraph() }
    single<ScheduleNavigation> { ScheduleNavigationImpl() }
    single<TrainingRepository> { if (isDebug) FakeTrainingRepository() else TrainingRepositoryImpl() }
    factory { SessionUiMapper(get(), get()) }

    factory<BookSessionUseCase> { BookSessionUseCaseImpl(get()) }
    factory<CancelBookingUseCase> { CancelBookingUseCaseImpl(get()) }
    factory<GetAllSessionsUseCase> { GetAllSessionsUseCaseImpl(get()) }
    factory<GetAvailableSessionsUseCase> { GetAvailableSessionsUseCaseImpl(get()) }
    factory<GetUserBookedSessionsUseCase> { GetUserBookedSessionsUseCaseImpl(get()) }

    viewModel {
        ScheduleViewModel(
            getAvailableSessionsUseCase = get(),
            getUserBookedSessionsUseCase = get(),
            getAllSessionsUseCase = get(),
            bookSessionUseCase = get(),
            cancelBookingUseCase = get(),
            mapper = get()
        )
    }
}
