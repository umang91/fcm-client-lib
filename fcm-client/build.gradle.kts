import java.io.FileInputStream
import java.util.*

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.plugin.android.library)
    alias(libs.plugins.plugin.kotlin.android)
    alias(libs.plugins.plugin.dokka)
}

fun getVersionName(): String {
    val properties = Properties()
    properties.load(FileInputStream("${project.rootDir.absolutePath}/fcm-client/gradle.properties"))
    return properties.getProperty("VERSION_NAME") ?: throw GradleException(
        "VERSION_NAME not found in ./fcm-client/gradle.properties"
    )
}

android {
    compileSdk = 30
    defaultConfig {
        minSdk = 21
        targetSdk = 30
        buildConfigField("String", "FCM_CLIENT_VERSION", "\"${getVersionName()}\"")
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

apply(plugin = "com.vanniktech.maven.publish")