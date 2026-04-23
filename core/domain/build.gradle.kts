plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.login.api)
            implementation(projects.features.schedule.api)
            implementation(projects.features.settings.api)
            implementation(projects.core.common)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(projects.features.login.api)
            implementation(projects.features.schedule.api)
            implementation(projects.features.settings.api)
        }
    }
}

android {
    namespace = "pt.dourobats.app.core.domain"
}
