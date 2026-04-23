package pt.dourobats.app.features.settings.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import pt.dourobats.app.features.settings.ui.SettingsViewModel
import pt.dourobats.app.features.settings.api.repository.SettingsRepository
import pt.dourobats.app.features.settings.data.SettingsRepositoryImpl

val settingsModule = module {
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }

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
