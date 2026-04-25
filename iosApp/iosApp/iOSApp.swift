import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        // Must be called before any Compose UI is created so Koin DI is ready.
        MainViewControllerKt.initializeApp()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}