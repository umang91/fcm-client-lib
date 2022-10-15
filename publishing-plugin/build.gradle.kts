plugins {
    kotlin("jvm") version "1.5.31"
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(kotlin("stdlib"))
}