package pt.dourobats.app.features.home.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import pt.dourobats.app.features.home.HomeViewModel

val homeModule = module {
    viewModel { HomeViewModel(observeUserProfile = get()) }
}
