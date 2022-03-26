buildscript {
    dependencies {
        classpath(libs.bundles.gradlePlugins)
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.plugin.android.application) apply false
    alias(libs.plugins.plugin.android.library) apply false
    alias(libs.plugins.plugin.kotlin.android) apply false
    alias(libs.plugins.plugin.dokka) apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

apply(plugin = "org.jetbrains.dokka")