package pt.dourobats.app.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import pt.dourobats.app.MainViewModel

val appModule = module {
    viewModel { MainViewModel(get(), get(), get()) }
}
