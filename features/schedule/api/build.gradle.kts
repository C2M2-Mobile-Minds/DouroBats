plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

android {
    namespace = "pt.dourobats.app.features.schedule.api"
}
