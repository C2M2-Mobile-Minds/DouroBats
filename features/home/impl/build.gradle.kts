plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
    id("pt.dourobats.app.compose")
}

compose.resources {
    packageOfResClass = "dourobats.features.home.generated.resources"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.login.api)
            implementation(projects.core.common)
            implementation(projects.core.ui)
            implementation(projects.features.schedule.api)
            implementation(projects.features.settings.api)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.koin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(projects.features.settings.testing)
        }
    }
}

android {
    namespace = "pt.dourobats.app.features.home.impl"
}
