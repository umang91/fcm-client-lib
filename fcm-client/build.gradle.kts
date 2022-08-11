@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.plugin.android.library)
    alias(libs.plugins.plugin.kotlin.android)
    alias(libs.plugins.plugin.dokka)
    `maven-publish`
    signing
}

val libVersionName = project.findProperty("VERSION_NAME") as String
val group = project.findProperty("GROUP") as String
val artifactName = project.findProperty("ARTIFACT_NAME") as String

val pomName = project.findProperty("POM_NAME") as String
val pomDescription = project.findProperty("POM_DESCRIPTION") as String
val projectUrl = project.findProperty("POM_URL") as String

val licenseName = project.findProperty("LICENCE_NAME") as String
val licenseUrl = project.findProperty("LICENCE_URL") as String

val developerId = project.findProperty("DEVELOPER_ID") as String
val developerName = project.findProperty("DEVELOPER_NAME") as String

val scmConnection = project.findProperty("SCM_CONNECTION") as String
val scmDevConnection = project.findProperty("SCM_DEV_CONNECTION") as String


android {
    compileSdk = 30
    namespace = "dev.assemblage.fcm.client"
    defaultConfig {
        minSdk = 21
        targetSdk = 30
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

val repositoryUsername = project.findProperty("mavenCentralUsername") as? String ?: ""
val repositoryPassword = project.findProperty("mavenCentralPassword") as? String ?: ""

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = group
            artifactId = artifactName
            version = libVersionName
            afterEvaluate {
                from(components["release"])
            }
            pom {
                name.set(pomName)
                description.set(pomDescription)
                url.set(projectUrl)
                licenses {
                    license {
                        name.set(licenseName)
                        url.set(licenseUrl)
                    }
                }
                developers {
                    developer {
                        id.set(developerId)
                        name.set(developerName)
                    }
                }
                scm {
                    connection.set(scmConnection)
                    developerConnection.set(scmDevConnection)
                    url.set(projectUrl)
                }
            }
        }
        repositories {
            maven {
                credentials {
                    username = repositoryUsername
                    password = repositoryPassword
                }
                url = if (libVersionName.endsWith("-SNAPSHOT")) {
                    uri("https://oss.sonatype.org/content/repositories/snapshots/")
                } else {
                    uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["release"])
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