plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
    id("pt.dourobats.app.compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.model)
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
