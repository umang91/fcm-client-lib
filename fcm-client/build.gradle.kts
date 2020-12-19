plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.dokka")
}

ext {
    set("bintrayRepo", BintrayConfig.repoName)
    set("publishedGroupId", BintrayConfig.publisherId)

    set("bintrayName", BintrayConfig.artifactName)
    set("libraryName", BintrayConfig.artifactId)
    set("artifact", BintrayConfig.artifactId)
    set("libraryDescription", BintrayConfig.description)
    set("libraryVersion", BintrayConfig.versionName)

    set("siteUrl", BintrayConfig.siteUrl)
    set("gitUrl", BintrayConfig.gitUrl)

    set("developerId", BintrayConfig.developerId)
    set("developerName", BintrayConfig.developerName)
    set("developerEmail", BintrayConfig.developerEmail)

    set("licenseName", BintrayConfig.licenseName)
    set("licenseUrl", BintrayConfig.licenseUrl)
    set("allLicenses", BintrayConfig.allLicenses)
}

android {
    compileSdkVersion(28)
    defaultConfig {
        minSdkVersion(16)
        targetSdkVersion(28)
        versionCode = BintrayConfig.versionCode
        versionName = BintrayConfig.versionName
        buildConfigField("String", "FCM_CLIENT_VERSION", "\"${BintrayConfig.versionName}\"")
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
apply(from = "https://raw.githubusercontent.com/umang91/jcenterScripts/master/installv.gradle")
apply(from = "https://raw.githubusercontent.com/umang91/jcenterScripts/master/bintray-kotlin.gradle")

tasks.dokkaHtml.configure {
    outputDirectory.set(rootDir.resolve("docs"))
}
