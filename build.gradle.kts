@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.plugin.android.application) apply false
    alias(libs.plugins.plugin.android.library) apply false
    alias(libs.plugins.plugin.kotlin.android) apply false
    alias(libs.plugins.plugin.dokka) apply false
    id("com.google.gms.google-services") version("4.3.5") apply false
    // id("org.jlleitschuh.gradle.ktlint") version("11.2.0")
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

apply(plugin = "org.jetbrains.dokka")