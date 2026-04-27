plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
    id("pt.dourobats.app.compose")
    alias(libs.plugins.kotlinSerialization)
}

compose.resources {
    packageOfResClass = "dourobats.features.management.generated.resources"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.features.management.api)
            implementation(projects.core.navigation)
            implementation(projects.core.common)
            implementation(projects.core.ui)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
    }
}

android {
    namespace = "pt.dourobats.app.features.management.impl"
}
