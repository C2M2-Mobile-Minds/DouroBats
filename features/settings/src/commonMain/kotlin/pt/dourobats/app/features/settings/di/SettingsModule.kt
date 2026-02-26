package pt.dourobats.app.features.settings.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import pt.dourobats.app.features.settings.SettingsViewModel

val settingsModule = module {
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
