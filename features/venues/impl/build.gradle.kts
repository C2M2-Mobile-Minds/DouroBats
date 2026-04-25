plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.features.venues.api)
            implementation(projects.core.common)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(projects.features.venues.testing)
        }
    }
}

android {
    namespace = "pt.dourobats.app.features.venues.impl"
}
