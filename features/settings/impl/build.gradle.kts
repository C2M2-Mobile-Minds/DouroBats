plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
    id("pt.dourobats.app.compose")
}

compose.resources {
    packageOfResClass = "dourobats.features.settings.generated.resources"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.features.settings.api)
            implementation(projects.core.domain)
            implementation(projects.core.data)
            implementation(projects.core.common)
            implementation(projects.core.ui)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.datastore.preferences)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.koin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(projects.features.settings.testing)
            implementation(projects.features.login.testing)
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.datastore.preferences.android)
            }
        }
    }
}

android {
    namespace = "pt.dourobats.app.features.settings.impl"
}
