plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.admin.api)
            implementation(projects.core.common)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}

android {
    namespace = "pt.dourobats.app.features.admin.testing"
}
