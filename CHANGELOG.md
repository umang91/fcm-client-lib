## Next Release
- Gradle plugins moved to version catalog.
- Update Android Gradle Plugin to 7.0
- Gradle version updated to 7.1.1
- Target SDK version updated to 30
- Compile SDK version updated to 30
- Minimum SDK version updated to 21
- Manifest components updated for Android 12.
- Removed deprecated method `initialise(Application, Logger.LogLevel, Long)`

## 3.0.0 (08-05-2021)
- Support for Lifecycle Callback `2.3.1` and above
- Target SDK version and Compile SDK version bumped to API Level `29`
- Gradle version updated to `7.0`
- Kotlin version updated `1.5.0`  
- Enabling Java 8
- Enabling Explicit API Mode
- Moving dependency management to version catalog
- Removed `jcenter()` dependency from the project
- Dokka version updated to 1.4.32

## 2.2.0 (19-02-2021)
- Artifact Publishing moved to Maven Central
- Moved to a new group-id `dev.assemblage`

## 2.1.0 (19-12-2020)
- Strict Mode violation fixed.
- Initialization in Application class is no longer mandatory
- Firebase dependency updated to `21.0.1`
- Gradle configuration now in Kotlin
- API documentation moved to new format and documentation url updated. New documentation can be
 found [here](https://umang91.github.io/fcm-client-lib/fcm-client/) 

## 2.0.2 (27-06-2020)
- Added an optional parameter in the `initialise()` to take in the retry interval.

## 2.0.1 (03-05-2020)
- Additional exception handling

## 2.0.0 (01-05-2020)
- Migration to AndroidX
- Migrating to all callbacks to a single listener
- Internal re-structuring
- Firebase Messaging Library updated to version `20.1.6`
- Kotlin version updated to `1.3.72`

## 1.4.0 (06-05-2019)
- Support for Gradle 5

## 1.2.0 (03-10-2018)
- Firebase version update 17.3.3
- Logger library update.

## 1.1.0 (29-09-2018)
- Updated Firebase version to 17.3.2
- Kotlin version updated to 1.2.71