package pt.dourobats.app.core.network.di

import io.ktor.client.HttpClient
import org.koin.dsl.module
import pt.dourobats.app.core.network.auth.NoOpTokenProvider
import pt.dourobats.app.core.network.auth.TokenProvider
import pt.dourobats.app.core.network.client.HttpClientFactory
import pt.dourobats.app.core.network.client.createHttpEngine

/**
 * Koin module for network dependencies.
 * Provides configured HttpClient and auth components.
 */
val networkModule = module {

    // Token provider - NoOp in Phase 1, will be replaced by auth module in Phase 2
    single<TokenProvider> { NoOpTokenProvider() }

    // HTTP Client - singleton, configured with all plugins
    single<HttpClient> {
        HttpClientFactory.create(
            engine = createHttpEngine(),
            tokenProvider = get()
        )
    }
}
