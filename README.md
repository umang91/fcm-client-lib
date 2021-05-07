![MavenBadge](https://maven-badges.herokuapp.com/maven-central/dev.assemblage/fcm-client/badge.svg)

## Firebase Messaging Client
A library which helps you handle Firebase Cloud Messaging(FCM). This library helps in abstracting out the logic firebase message lifecycle, it takes care of token lifecycle and delivers the notification payload received from FCM to the application via a callback listener.
The library internally manages token registration, token refresh and also has a retry mechanism in case of registration failure.   

## Features

- FCM token registration
- FCM token rotation
- Callback for token registration change
- Callback for FCM messages
- Retry mechanism on timeouts or registration failures
- Fallback for missed token rotation callback
- Subscribe/un-subscribe to topics

## Installation

Add the below line in the dependency block of your app's `build.gradle`

```groovy
implementation("dev.assemblage:fcm-client:$sdkVersion")
```

replace `$sdkVersion` with the latest version of the SDK

## Set-up Firebase
Before initialising the library make sure you have added the required plugins and `google-services.json` file into your project. Refer to the [documentation](https://firebase.google.com/docs/android/setup)
for more details.

<b>Note:</b> Dependencies and listeners required for the Firebase Messaging to work is already
 added in the library, you need not add it again. 

## Initialisation

Initialise the FCM client SDK in the `onCreate()` of the Application subclass. To initialise add 
the below line.

```kotlin
FcmClientHelper.getInstance(applicationContext).initialise()
```

Optionally you can set the log level and retry interval in the initialise API, refer to the API
 documentation for more details.

## Set-up Callback Listener

To receive a callback whenever a token is generated or notification is received implement the
 `FirebaseMessageListener` interface and register a listener using the `addListener()` of the
  `FcmClientHelper`. Please ensure this callback is registered in the `onCreate()` of the
   Application sub-class of your application. 
Below is an example of setting up the callback in the  `onCreate()` of Application class

```kotlin
FcmClientHelper.getInstance(applicationContext).addListener(FirebaseListener())
```

<b>Token callback:</b> `onTokenAvailable()` is called whenever a new token is available.

<b>Push Payload callback:</b> `onPushReceived()` is called a push notification is received on the
  device.
  
## Excluding files from back-up
Auto Backup for Apps automatically backs up a user's data from apps that target and run on
Android 6.0 (API level 23) or later. As a general recommendation push tokens should not be
backed up as it gets invalidated over time. So to ensure token is not backed up exclude the
shared preference file from the back-up.

To exclude the file include the below in your app's backup descriptor

```xml
    <exclude
        domain="sharedpref"
        path="umang_fcm_client_lib" />

``` 

If your app does not have a back-up descriptor already you can directly add the back-up
 descriptor provided by the SDK as shown below
 
```xml
<application

  android:fullBackupContent="@xml/fcm_client_backup_descriptor">
</application>
```

## Topics Messaging

Library supports topic messaging. The app can subscribe/un-subscribe to messages whenever required.

API for subscribing to topic
 
 ```kotlin
FcmClientHelper.getInstance(applicationContext).subscribeToTopics()
```

API for un-subscribing to topic

```kotlin
FcmClientHelper.getInstance(applicationContext).unSubscribeTopic()
```

## Enable logging

By default only info logs are enabled to enable verbose logging pass in the log-level in the
 `initialise()` as shown below
 
 ```kotlin
FcmClientHelper.getInstance(applicationContext).initialise(this, Logger.LogLevel.VERBOSE)
```  

Refer to the API [documentation](https://umang91.github.io/fcm-client-lib/index.html) for more details.