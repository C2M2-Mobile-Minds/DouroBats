plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
    id("pt.dourobats.app.compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.core.ui)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.datetime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.koin.test)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(libs.compose.ui.test)
        }
        androidInstrumentedTest.dependencies {
            implementation(libs.compose.ui.test.junit4)
            implementation(libs.compose.ui.test.manifest)
        }
    }
}

android {
    namespace = "pt.dourobats.app.features.schedule"
}
