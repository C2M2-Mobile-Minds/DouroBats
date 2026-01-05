import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            val composeExt = extensions.getByType<ComposeExtension>()

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    commonMain {
                        dependencies {
                            implementation(composeExt.dependencies.runtime)
                            implementation(composeExt.dependencies.foundation)
                            implementation(composeExt.dependencies.material3)
                            implementation(composeExt.dependencies.materialIconsExtended)
                            implementation(composeExt.dependencies.ui)
                            implementation(composeExt.dependencies.components.resources)
                            implementation(composeExt.dependencies.components.uiToolingPreview)
                            implementation(libs.findLibrary("androidx-lifecycle-viewmodelCompose").get())
                            implementation(libs.findLibrary("androidx-lifecycle-runtimeCompose").get())
                        }
                    }
                    androidMain {
                        dependencies {
                            implementation(composeExt.dependencies.preview)
                            implementation(libs.findLibrary("androidx-activity-compose").get())
                        }
                    }
                    commonTest {
                        dependencies {
                            implementation(libs.findLibrary("kotlin-test").get())
                        }
                    }
                }
            }

            dependencies {
                add("debugImplementation", composeExt.dependencies.uiTooling)
            }
        }
    }
}
