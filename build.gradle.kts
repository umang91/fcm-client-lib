@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.plugin.android.application) apply false
    alias(libs.plugins.plugin.android.library) apply false
    alias(libs.plugins.plugin.kotlin.android) apply false
    alias(libs.plugins.plugin.dokka) apply false
    alias(libs.plugins.plugin.gms) apply false
    alias(libs.plugins.plugin.ktlint)
    alias(libs.plugins.plugin.publish) apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

apply(plugin = "org.jetbrains.dokka")

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}
