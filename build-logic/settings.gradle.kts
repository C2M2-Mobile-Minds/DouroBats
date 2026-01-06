rootProject.name = "build-logic"
include(":convention")

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    versionCatalogs {
        create("libs") {
            from(files("${rootDir.parent}/gradle/libs.versions.toml"))
        }
    }
}