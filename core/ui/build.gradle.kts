plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
    id("pt.dourobats.app.compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.schedule.api)
            implementation(projects.features.settings.api)
            api(libs.kotlinx.datetime)  // API because we expose kotlinx.datetime types in public API
        }
    }
}

compose.resources {
    publicResClass = true
}

android {
    namespace = "pt.dourobats.app.core.ui"
}
