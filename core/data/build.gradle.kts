plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.login.api)
            implementation(projects.features.schedule.api)
            implementation(projects.features.settings.api)
            implementation(projects.core.common)
            implementation(projects.core.network)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(libs.androidx.datastore.preferences.android)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

android {
    namespace = "pt.dourobats.app.core.data"
}
