plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            // BuildConfig.DEBUG available via android.library plugin
        }
    }
}

android {
    namespace = "pt.dourobats.app.core.common"
    buildFeatures {
        buildConfig = true
    }
}
