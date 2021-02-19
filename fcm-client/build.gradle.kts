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
    compileSdkVersion(28)
    defaultConfig {
        minSdkVersion(16)
        targetSdkVersion(28)
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
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(Deps.kotlin)
    api(Deps.fcm)
    api(Deps.processLifecycle)
}

tasks.dokkaHtml.configure {
    outputDirectory.set(rootDir.resolve("docs"))
}

apply(plugin = "com.vanniktech.maven.publish")