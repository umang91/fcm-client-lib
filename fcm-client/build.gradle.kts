@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.plugin.android.library)
    alias(libs.plugins.plugin.kotlin.android)
    alias(libs.plugins.plugin.dokka)
    id("mvn-publish")
}

val libVersionName = project.findProperty("VERSION_NAME") as String

android {
    compileSdk = 31
    namespace = "dev.assemblage.fcm.client"
    defaultConfig {
        minSdk = 21
        targetSdk = 31
        buildConfigField("String", "FCM_CLIENT_VERSION", "\"$libVersionName\"")
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
        freeCompilerArgs = freeCompilerArgs + "-Xexplicit-api=strict"
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(libs.kotlin.stdLib)
    api(libs.processLifecycle)
    api(libs.fcm)
}

tasks.dokkaHtml.configure {
    outputDirectory.set(rootDir.resolve("docs"))
}