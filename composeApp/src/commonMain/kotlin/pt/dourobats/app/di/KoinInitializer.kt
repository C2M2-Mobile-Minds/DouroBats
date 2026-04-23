package pt.dourobats.app.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import pt.dourobats.app.core.data.di.dataModule
import pt.dourobats.app.core.network.di.networkModule
import pt.dourobats.app.di.appModule
import pt.dourobats.app.features.login.di.loginModule
import pt.dourobats.app.features.home.di.homeModule
import pt.dourobats.app.features.schedule.di.scheduleModule
import pt.dourobats.app.features.settings.di.settingsModule

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(
            appModule,
            networkModule,
            dataModule,
            loginModule,
            scheduleModule,
            settingsModule,
            homeModule,
        )
    }
}
