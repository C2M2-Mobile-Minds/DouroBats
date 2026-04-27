package pt.dourobats.app.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import pt.dourobats.app.core.common.di.commonModule
import pt.dourobats.app.core.data.di.dataModule
import pt.dourobats.app.core.navigation.di.navigationModule
import pt.dourobats.app.core.network.di.networkModule
import pt.dourobats.app.features.admin.di.adminModule
import pt.dourobats.app.features.home.di.homeModule
import pt.dourobats.app.features.login.di.loginModule
import pt.dourobats.app.features.management.di.managementModule
import pt.dourobats.app.features.schedule.di.scheduleModule
import pt.dourobats.app.features.settings.di.settingsModule
import pt.dourobats.app.features.venues.di.venuesModule

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(
            commonModule,
            appModule,
            navigationModule,
            networkModule,
            dataModule,
            loginModule,
            scheduleModule,
            settingsModule,
            homeModule,
            managementModule,
            venuesModule,
            adminModule,
        )
    }
}
