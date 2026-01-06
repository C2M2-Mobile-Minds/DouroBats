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
            from(files(layout.projectDirectory.file("../gradle/libs.versions.toml")))
        }
    }
}