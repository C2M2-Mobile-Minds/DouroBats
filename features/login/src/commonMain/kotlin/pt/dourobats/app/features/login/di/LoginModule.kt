package pt.dourobats.app.features.login.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import pt.dourobats.app.core.domain.usecase.LoginWithEmailUseCase
import pt.dourobats.app.core.domain.usecase.LoginWithSocialUseCase
import pt.dourobats.app.core.domain.usecase.LogoutUseCase
import pt.dourobats.app.features.login.LoginErrorMapper
import pt.dourobats.app.features.login.LoginFormValidator
import pt.dourobats.app.features.login.LoginViewModel

/**
 * Koin module for the login feature.
 *
 * Registers:
 * - Use cases (domain layer business logic)
 * - Validators (UI-level validation)
 * - Error mappers (domain to UI error translation)
 * - ViewModels (presentation layer orchestration)
 *
 * Following Clean Architecture, dependencies flow inward:
 * ViewModel -> Use Cases -> Repository
 */
val loginModule = module {
    // Use cases (domain layer) - created fresh for each use
    factoryOf(::LoginWithEmailUseCase)
    factoryOf(::LoginWithSocialUseCase)
    factoryOf(::LogoutUseCase)

    // Utilities (presentation layer) - created fresh for each use
    factoryOf(::LoginFormValidator)
    factoryOf(::LoginErrorMapper)

    // ViewModel (presentation layer) - created fresh for each screen instance
    viewModel {
        LoginViewModel(
            loginWithEmailUseCase = get(),
            loginWithSocialUseCase = get(),
            validator = get(),
            errorMapper = get()
        )
    }
}
