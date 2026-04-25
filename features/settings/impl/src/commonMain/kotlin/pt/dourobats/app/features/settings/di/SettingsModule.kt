package pt.dourobats.app.features.settings.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import pt.dourobats.app.features.settings.api.usecase.ObserveLanguageUseCase
import pt.dourobats.app.features.settings.api.usecase.ObserveThemeUseCase
import pt.dourobats.app.features.settings.api.usecase.ObserveUserProfileUseCase
import pt.dourobats.app.features.settings.api.usecase.SetLanguageUseCase
import pt.dourobats.app.features.settings.api.usecase.SetThemeUseCase
import pt.dourobats.app.features.settings.api.usecase.UpdateUserProfileUseCase
import pt.dourobats.app.features.settings.data.SettingsRepositoryImpl
import pt.dourobats.app.features.settings.repository.SettingsRepository
import pt.dourobats.app.features.settings.ui.SettingsViewModel
import pt.dourobats.app.features.settings.SettingsNavigation
import pt.dourobats.app.features.settings.navigation.SettingsNavigationImpl
import pt.dourobats.app.features.settings.usecase.ObserveLanguageUseCaseImpl
import pt.dourobats.app.features.settings.usecase.ObserveThemeUseCaseImpl
import pt.dourobats.app.features.settings.usecase.ObserveUserProfileUseCaseImpl
import pt.dourobats.app.features.settings.usecase.SetLanguageUseCaseImpl
import pt.dourobats.app.features.settings.usecase.SetThemeUseCaseImpl
import pt.dourobats.app.features.settings.usecase.UpdateUserProfileUseCaseImpl

val settingsModule = module {
    single<SettingsNavigation> { SettingsNavigationImpl() }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }

    factory<ObserveLanguageUseCase> { ObserveLanguageUseCaseImpl(get()) }
    factory<ObserveThemeUseCase> { ObserveThemeUseCaseImpl(get()) }
    factory<ObserveUserProfileUseCase> { ObserveUserProfileUseCaseImpl(get()) }
    factory<SetLanguageUseCase> { SetLanguageUseCaseImpl(get()) }
    factory<SetThemeUseCase> { SetThemeUseCaseImpl(get()) }
    factory<UpdateUserProfileUseCase> { UpdateUserProfileUseCaseImpl(get()) }

    viewModel {
        SettingsViewModel(
            observeUserProfile = get(),
            observeLanguage = get(),
            observeTheme = get(),
            setLanguageUseCase = get(),
            setThemeUseCase = get(),
            updateUserProfileUseCase = get(),
            logoutUseCase = get()
        )
    }
}
