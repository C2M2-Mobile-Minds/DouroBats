plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.schedule.api)
            implementation(projects.core.domain)
            implementation(projects.core.common)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
        }
    }
}

android {
    namespace = "pt.dourobats.app.features.schedule.testing"
}
