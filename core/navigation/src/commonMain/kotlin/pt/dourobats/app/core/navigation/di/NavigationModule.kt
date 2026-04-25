package pt.dourobats.app.core.navigation.di

import org.koin.dsl.module
import pt.dourobats.app.core.navigation.NavigationManager

val navigationModule = module {
    single { NavigationManager() }
}
