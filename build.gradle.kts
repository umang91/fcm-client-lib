// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    extra.set("kotlinVersion", "1.5.0")
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${rootProject.extra.get("kotlinVersion")}")
        classpath("com.google.gms:google-services:4.3.5")
        // publishing plugin
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.14.2")
        // dokka plugin for documentation
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.4.32")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}