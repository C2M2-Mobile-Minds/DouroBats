plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(projects.core.common)
        }
    }
}

android {
    namespace = "pt.dourobats.app.features.login.api"
}
