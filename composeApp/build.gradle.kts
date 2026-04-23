plugins {
    id("pt.dourobats.app.android.application")
    id("pt.dourobats.app.kmp")
    id("pt.dourobats.app.compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Feature modules (:impl deps expose :api transitively)
            implementation(projects.features.home.api)
            implementation(projects.features.home.impl)
            implementation(projects.features.login.impl)
            implementation(projects.features.schedule.impl)
            implementation(projects.features.settings.impl)

            // Core modules
            implementation(projects.core.common)
            implementation(projects.core.data)
            implementation(projects.core.network)
            implementation(projects.core.ui)

            // Navigation
            implementation(libs.androidx.navigation.compose)

            // Dependency Injection
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}

android {
    namespace = "pt.dourobats.app"

    defaultConfig {
        applicationId = "pt.dourobats.app"
        versionCode = 1
        versionName = "1.0"
    }
}

