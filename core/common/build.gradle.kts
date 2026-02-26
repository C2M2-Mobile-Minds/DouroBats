plugins {
    id("pt.dourobats.app.android.library")
    id("pt.dourobats.app.kmp")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // No dependencies - this is the foundation layer
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
