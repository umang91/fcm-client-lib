import com.diffplug.spotless.kotlin.KtfmtStep.TrailingCommaManagementStrategy

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.plugin.android.application) apply false
    alias(libs.plugins.plugin.android.library) apply false
    alias(libs.plugins.plugin.kotlin.android) apply false
    alias(libs.plugins.plugin.dokka) apply false
    alias(libs.plugins.plugin.gms) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.plugin.publish) apply false
}

/*
tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
*/

apply(plugin = "org.jetbrains.dokka")

subprojects {
    apply(plugin = "com.diffplug.spotless")

    spotless {
        kotlin {
            ktfmt("0.62").kotlinlangStyle().configure {
                it.setMaxWidth(100)
                it.setTrailingCommaManagementStrategy(TrailingCommaManagementStrategy.NONE)
            }
            target("src/**/*.kt") // Specify which files to format
            targetExclude("**/build/**") // Exclude files in build directories
        }
    }
}
