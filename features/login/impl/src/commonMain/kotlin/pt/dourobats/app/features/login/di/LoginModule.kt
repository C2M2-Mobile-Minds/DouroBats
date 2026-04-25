package pt.dourobats.app.features.login.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import pt.dourobats.app.features.login.api.usecase.InvalidateSessionUseCase
import pt.dourobats.app.features.login.api.usecase.LogoutUseCase
import pt.dourobats.app.features.login.api.usecase.ObserveAuthStateUseCase
import pt.dourobats.app.features.login.api.usecase.RequestLoginCodeUseCase
import pt.dourobats.app.features.login.api.usecase.VerifyLoginCodeUseCase
import pt.dourobats.app.features.login.data.AuthRepositoryImpl
import pt.dourobats.app.features.login.repository.AuthRepository
import pt.dourobats.app.features.login.ui.LoginErrorMapper
import pt.dourobats.app.features.login.ui.LoginFormValidator
import pt.dourobats.app.features.login.ui.LoginViewModel
import pt.dourobats.app.features.login.usecase.InvalidateSessionUseCaseImpl
import pt.dourobats.app.features.login.usecase.LogoutUseCaseImpl
import pt.dourobats.app.features.login.usecase.ObserveAuthStateUseCaseImpl
import pt.dourobats.app.features.login.usecase.RequestLoginCodeUseCaseImpl
import pt.dourobats.app.features.login.usecase.VerifyLoginCodeUseCaseImpl

val loginModule = module {
    single<AuthRepository> { AuthRepositoryImpl(dataStore = get(), logger = get()) }

    factory<RequestLoginCodeUseCase> { RequestLoginCodeUseCaseImpl(get()) }
    factory<VerifyLoginCodeUseCase> { VerifyLoginCodeUseCaseImpl(get()) }
    factory<LogoutUseCase> { LogoutUseCaseImpl(get()) }
    factory<InvalidateSessionUseCase> { InvalidateSessionUseCaseImpl(get()) }
    factory<ObserveAuthStateUseCase> { ObserveAuthStateUseCaseImpl(get()) }

    factoryOf(::LoginFormValidator)
    factoryOf(::LoginErrorMapper)

    viewModel {
        LoginViewModel(
            requestLoginCode = get(),
            verifyLoginCode = get(),
            validator = get(),
            errorMapper = get(),
            observeLanguage = get(),
            setLanguageUseCase = get(),
        )
    }
}
