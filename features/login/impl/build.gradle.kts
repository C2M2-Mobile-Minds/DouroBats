plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
    id("pt.dourobats.app.compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.features.login.api)
            implementation(projects.core.domain)
            implementation(projects.core.model)
            implementation(projects.core.common)
            implementation(projects.core.ui)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.androidx.navigation.compose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.koin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(projects.core.test)
        }
    }
}

android {
    namespace = "pt.dourobats.app.features.login.impl"
}
