plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            api(projects.features.login.api)
            api(projects.core.localization)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "pt.dourobats.app.features.settings.api"
}
