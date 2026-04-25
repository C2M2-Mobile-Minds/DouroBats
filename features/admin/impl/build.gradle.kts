plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.features.admin.api)
            implementation(projects.features.schedule.api)
            implementation(projects.core.common)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(projects.features.admin.testing)
        }
    }
}

android {
    namespace = "pt.dourobats.app.features.admin.impl"
}
