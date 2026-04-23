plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
    id("pt.dourobats.app.compose")
}

compose.resources {
    packageOfResClass = "dourobats.features.schedule.generated.resources"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.features.schedule.api)
            implementation(projects.core.domain)
            implementation(projects.core.common)
            implementation(projects.core.ui)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.kotlinx.datetime)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.koin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(projects.features.schedule.testing)
        }
    }
}

android {
    namespace = "pt.dourobats.app.features.schedule.impl"
}
