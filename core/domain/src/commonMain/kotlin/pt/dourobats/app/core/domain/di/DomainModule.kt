package pt.dourobats.app.core.domain.di

import org.koin.dsl.module
import pt.dourobats.app.core.domain.usecase.BookSessionUseCase
import pt.dourobats.app.core.domain.usecase.BookSessionUseCaseImpl
import pt.dourobats.app.core.domain.usecase.CancelBookingUseCase
import pt.dourobats.app.core.domain.usecase.CancelBookingUseCaseImpl
import pt.dourobats.app.core.domain.usecase.GetAllSessionsUseCase
import pt.dourobats.app.core.domain.usecase.GetAllSessionsUseCaseImpl
import pt.dourobats.app.core.domain.usecase.GetAvailableSessionsUseCase
import pt.dourobats.app.core.domain.usecase.GetAvailableSessionsUseCaseImpl
import pt.dourobats.app.core.domain.usecase.GetUserBookedSessionsUseCase
import pt.dourobats.app.core.domain.usecase.GetUserBookedSessionsUseCaseImpl
import pt.dourobats.app.core.domain.usecase.LoginWithEmailUseCase
import pt.dourobats.app.core.domain.usecase.LoginWithEmailUseCaseImpl
import pt.dourobats.app.core.domain.usecase.LoginWithSocialUseCase
import pt.dourobats.app.core.domain.usecase.LoginWithSocialUseCaseImpl
import pt.dourobats.app.core.domain.usecase.LogoutUseCase
import pt.dourobats.app.core.domain.usecase.LogoutUseCaseImpl

/**
 * Koin module for the domain layer.
 *
 * Registers all use cases (business logic) that orchestrate operations
 * between repositories and provide clean interfaces to the presentation layer.
 *
 * Use cases are registered as factories so a new instance is created for each
 * invocation, ensuring thread safety and avoiding shared state issues.
 */
val domainModule = module {
    // Authentication use cases
    factory<LoginWithEmailUseCase> { LoginWithEmailUseCaseImpl(get()) }
    factory<LoginWithSocialUseCase> { LoginWithSocialUseCaseImpl(get()) }
    factory<LogoutUseCase> { LogoutUseCaseImpl(get()) }

    // Session management use cases
    factory<GetAllSessionsUseCase> { GetAllSessionsUseCaseImpl(get()) }
    factory<GetAvailableSessionsUseCase> { GetAvailableSessionsUseCaseImpl(get()) }
    factory<GetUserBookedSessionsUseCase> { GetUserBookedSessionsUseCaseImpl(get()) }
    factory<BookSessionUseCase> { BookSessionUseCaseImpl(get()) }
    factory<CancelBookingUseCase> { CancelBookingUseCaseImpl(get()) }
}
