package pt.dourobats.app.core.data.di

import org.koin.dsl.module
import pt.dourobats.app.core.data.preferences.createDataStore

val dataModule = module {
    single { createDataStore() }
}
