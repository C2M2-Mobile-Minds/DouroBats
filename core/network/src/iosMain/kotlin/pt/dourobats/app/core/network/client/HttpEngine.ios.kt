package pt.dourobats.app.core.network.client

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

/**
 * Provides the iOS-specific HTTP engine (Darwin/URLSession).
 */
actual fun createHttpEngine(): HttpClientEngine = Darwin.create()
