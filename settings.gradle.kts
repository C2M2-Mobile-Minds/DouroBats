rootProject.name = "DouroBats"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")

// Core modules
include(":core:common")
include(":core:data")
include(":core:network")
include(":core:ui")
// Feature modules
include(":features:home:api")
include(":features:home:impl")
include(":features:login:api")
include(":features:login:impl")
include(":features:login:testing")
include(":features:schedule:api")
include(":features:schedule:impl")
include(":features:schedule:testing")
include(":features:settings:api")
include(":features:settings:impl")
include(":features:settings:testing")