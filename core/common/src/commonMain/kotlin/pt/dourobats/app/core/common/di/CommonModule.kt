package pt.dourobats.app.core.common.di

import org.koin.dsl.module
import pt.dourobats.app.core.common.logging.Logger
import pt.dourobats.app.core.common.logging.createPlatformLogger

val commonModule = module {
    single<Logger> { createPlatformLogger() }
}
