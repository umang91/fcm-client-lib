// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(libs.bundles.gradlePlugins)
    }
}

plugins {
    id("com.android.application") version "7.1.1" apply false
    id("com.android.library") version "7.1.1" apply false
    id("org.jetbrains.kotlin.android") version libs.versions.kotlinVersion apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}