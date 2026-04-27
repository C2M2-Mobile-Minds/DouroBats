package pt.dourobats.app.features.home.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import pt.dourobats.app.core.navigation.FeatureGraph
import pt.dourobats.app.features.home.HomeGraph
import pt.dourobats.app.features.home.HomeNavigation
import pt.dourobats.app.features.home.navigation.HomeNavigationImpl
import pt.dourobats.app.features.home.ui.HomeViewModel

val homeModule = module {
    single<FeatureGraph>(named("home")) { HomeGraph() }
    single<HomeNavigation> { HomeNavigationImpl(navigationManager = get()) }
    viewModel { HomeViewModel(observeUserProfile = get(), navigation = get()) }
}
