package pt.dourobats.app.features.management.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import pt.dourobats.app.core.navigation.FeatureGraph
import pt.dourobats.app.features.management.ManagementGraph
import pt.dourobats.app.features.management.ui.ManagementViewModel

val managementModule = module {
    single<FeatureGraph>(named("management")) { ManagementGraph() }
    viewModel { ManagementViewModel() }
}
