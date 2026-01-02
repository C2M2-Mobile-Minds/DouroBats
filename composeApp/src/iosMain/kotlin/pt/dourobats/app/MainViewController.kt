package pt.dourobats.app

import androidx.compose.ui.window.ComposeUIViewController
import pt.dourobats.app.di.initKoin

fun MainViewController() = ComposeUIViewController {
    // Initialize Koin for iOS
    initKoin()

    App()
}