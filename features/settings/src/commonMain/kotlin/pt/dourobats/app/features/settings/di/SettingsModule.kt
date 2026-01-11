package pt.dourobats.app.features.settings.di

import org.koin.core.module.dsl.*
import org.koin.dsl.module
import pt.dourobats.app.features.settings.SettingsViewModel

/**
 * Koin module for the settings feature.
 *
 * ViewModel dependencies:
 * - SettingsRepository for user preferences
 * - LogoutUseCase for handling logout (following Clean Architecture)
 */
val settingsModule = module {
    viewModel {
        SettingsViewModel(
            settingsRepository = get(),
            logoutUseCase = get()
        )
    }
}
