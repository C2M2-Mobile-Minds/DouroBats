plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
    id("pt.dourobats.app.compose")
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

android {
    namespace = "pt.dourobats.app.features.home.api"
}
