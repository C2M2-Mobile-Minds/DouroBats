plugins {
    id("pt.dourobats.app.android.application")
    id("pt.dourobats.app.kmp")
    id("pt.dourobats.app.compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Feature modules
            implementation(projects.features.home)
            implementation(projects.features.schedule)
            implementation(projects.features.settings)

            // Core modules
            implementation(projects.core.domain)
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

