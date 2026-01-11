package pt.dourobats.app.core.data.di

import org.koin.dsl.module
import pt.dourobats.app.core.data.preferences.createDataStore
import pt.dourobats.app.core.data.repository.AuthRepositoryImpl
import pt.dourobats.app.core.data.repository.FakeTrainingRepository
import pt.dourobats.app.core.data.repository.SettingsRepositoryImpl
import pt.dourobats.app.core.domain.repository.AuthRepository
import pt.dourobats.app.core.domain.repository.SettingsRepository
import pt.dourobats.app.core.domain.repository.TrainingRepository

val dataModule = module {
    single { createDataStore() }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<TrainingRepository> { FakeTrainingRepository() }
}
