plugins {
    alias(libs.plugins.plugin.android.application)
    alias(libs.plugins.plugin.kotlin.android)
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.umang.example.fcmclient"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation(libs.kotlin.stdLib)
    implementation(project(":fcm-client"))
}
apply(plugin= "com.google.gms.google-services")
