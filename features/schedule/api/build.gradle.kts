plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
    id("pt.dourobats.app.compose")
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutines.core)
            implementation(projects.core.common)
            api(projects.core.ui)
            implementation(projects.core.localization)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

compose.resources {
    packageOfResClass = "dourobats.features.schedule.api.generated.resources"
}

android {
    namespace = "pt.dourobats.app.features.schedule.api"
}
