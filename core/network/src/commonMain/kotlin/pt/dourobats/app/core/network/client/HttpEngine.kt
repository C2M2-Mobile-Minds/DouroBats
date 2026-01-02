package pt.dourobats.app.core.network.client

import io.ktor.client.engine.HttpClientEngine

/**
 * Platform-specific HTTP engine.
 * Actual implementations:
 * - Android: OkHttp
 * - iOS: Darwin (URLSession)
 */
expect fun createHttpEngine(): HttpClientEngine
