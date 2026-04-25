plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            api(libs.kotlinx.coroutines.core) // Changed to api to export Flow
            implementation(projects.core.common)
            api(projects.features.schedule.api) // Changed to api to export Session
            implementation(projects.features.venues.api)
            implementation(projects.features.login.api)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "pt.dourobats.app.features.admin.api"
}
