plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.dokka")
}

ext {
    set(PomKeys.artifactId, ReleaseConfig.artifactId)
    set(PomKeys.description, ReleaseConfig.description)
    set(PomKeys.name, ReleaseConfig.artifactName)
    set(PomKeys.versionName, ReleaseConfig.versionName)
}

android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(16)
        targetSdkVersion(29)
        versionCode = ReleaseConfig.versionCode
        versionName = ReleaseConfig.versionName
        buildConfigField("String", "FCM_CLIENT_VERSION", "\"${ReleaseConfig.versionName}\"")
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
        freeCompilerArgs += "-Xexplicit-api=strict"
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(Deps.kotlin)
    api(libs.processLifecycle)
    api(libs.fcm)
}

tasks.dokkaHtml.configure {
    outputDirectory.set(rootDir.resolve("docs"))
}

apply(plugin = "com.vanniktech.maven.publish")