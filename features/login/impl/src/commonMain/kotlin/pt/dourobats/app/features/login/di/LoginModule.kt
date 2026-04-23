package pt.dourobats.app.features.login.di

import org.koin.core.module.dsl.*
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import pt.dourobats.app.core.domain.di.domainModule
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
    includes(domainModule)


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
