package pt.dourobats.app.features.login.di

import org.koin.core.module.dsl.*
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import pt.dourobats.app.core.domain.usecase.LoginWithEmailUseCase
import pt.dourobats.app.core.domain.usecase.LoginWithSocialUseCase
import pt.dourobats.app.core.domain.usecase.LogoutUseCase
import pt.dourobats.app.core.domain.usecase.ObserveAuthStateUseCase
import pt.dourobats.app.features.login.data.AuthRepositoryImpl
import pt.dourobats.app.features.login.repository.AuthRepository
import pt.dourobats.app.features.login.ui.LoginErrorMapper
import pt.dourobats.app.features.login.ui.LoginFormValidator
import pt.dourobats.app.features.login.ui.LoginViewModel
import pt.dourobats.app.features.login.usecase.LoginWithEmailUseCaseImpl
import pt.dourobats.app.features.login.usecase.LoginWithSocialUseCaseImpl
import pt.dourobats.app.features.login.usecase.LogoutUseCaseImpl
import pt.dourobats.app.features.login.usecase.ObserveAuthStateUseCaseImpl

val loginModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }

    factory<LoginWithEmailUseCase> { LoginWithEmailUseCaseImpl(get()) }
    factory<LoginWithSocialUseCase> { LoginWithSocialUseCaseImpl(get()) }
    factory<LogoutUseCase> { LogoutUseCaseImpl(get()) }
    factory<ObserveAuthStateUseCase> { ObserveAuthStateUseCaseImpl(get()) }

    factoryOf(::LoginFormValidator)
    factoryOf(::LoginErrorMapper)

    viewModel {
        LoginViewModel(
            loginWithEmailUseCase = get(),
            loginWithSocialUseCase = get(),
            validator = get(),
            errorMapper = get()
        )
    }
}
