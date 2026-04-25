package pt.dourobats.app

import androidx.compose.ui.window.ComposeUIViewController
import pt.dourobats.app.di.initKoin

/**
 * Entry point for the iOS Compose UI.
 * NOTE: [initializeApp] MUST be called before this (e.g., in iOSApp.init()) —
 * otherwise Koin DI is not ready and ViewModel injection will crash.
 */
fun MainViewController() = ComposeUIViewController { App() }

/** Call once at app startup (before any Compose UI) to initialize Koin DI. */
fun initializeApp() = initKoin()
