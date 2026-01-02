package pt.dourobats.app.core.network.client

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

/**
 * Provides the Android-specific HTTP engine (OkHttp).
 */
actual fun createHttpEngine(): HttpClientEngine = OkHttp.create()
