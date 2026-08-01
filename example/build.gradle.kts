@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.plugin.android.application)
    alias(libs.plugins.plugin.gms)
}

android {
    compileSdk = 34

    defaultConfig {
        namespace = "dev.assemblage.fcm.client.example"
        applicationId = "dev.assemblage.fcm.client.example"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation(libs.kotlin.stdLib)
    implementation(project(":fcm-client"))
}
apply(plugin = "com.google.gms.google-services")
