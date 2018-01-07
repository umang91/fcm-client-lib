# FCMClientLib
A library which helps you register for Firebase Cloud Messaging(FCM) and listen for FCM 
messages. It can be used to attach multiple push providers and deliver data and token to 
all the listeners. This library is very helpful for those you use multiple push notification 
provider.

# Features

* FCM token registration
* FCM token rotation
* Callback for token registration change
* Callback for FCM messages
* Retry mechanism on timeouts or registration failures
* Fallback for missed token rotation callback
* Subscribe/un-subscribe to topics 


# Usage

**Initialisation**

Initialise the FCM client SDK in the `onCreate()` of the Application subclass. To initialise add 
the below line.

```
FCMClientHelper.newInstance(applicationContext).initializeFCMClient(this)
```

**Get push token**

The library passes the push token to the client app via a callback. 
It is recommended to register for the listener in the `onCreate()` of the application class. If the 
listener is set in other places such as activity/fragment token refresh callbacks might be missed.

**Setup token callback**

To get a callback one should implement the `FCMClientHelper.TokenReceivedListener` 
provided by the library and pass an instance of the same to the `FCMClientHelper.newInstance
(applicationContext).registerTokenRegistrationListener()`.

Below is a sample implementation of the interface

```
class TokenReceiver:FCMClientHelper.TokenReceivedListener {
  override fun onTokenReceived(token: String) {
    Log.v("SampleApplication", "TokenReceiver : $token")
    //save token or send to server or pass to other sdks
  }
}

```

Below is an example of setting up the callback in the  `onCreate()` of Application class

```
FCMClientHelper.newInstance(applicationContext).registerTokenRegistrationListener(TokenReceiver())
```

**Get push payload**

The library passes the push payload to the client app via a callback.
It is recommended to register for the listener in the `onCreate()` of the application class. If the 
listener is set in other places such as activity/fragment token refresh callbacks might be missed.

**Setup push payload callback**

To get a callback one should implement `FCMClientHelper.PushReceivedListener` provided by the 
library and pass an instance of the same to `FCMClientHelper.newInstance(applicationContext)
.registerPushReceivedListener()`

Below is a sample implementation of the interface

```
class PushReceiver: FCMClientHelper.PushReceivedListener {
  override fun onPushReceived(remoteMessage: RemoteMessage) {
    for ((key, value) in remoteMessage.data){
      Log.v("SampleApplication", "Key: $key, Value: $value")
    }
    //display push or pass to other sdks if required
  }
}

```

Below is an example of setting up the callback in the  `onCreate()` of Application class

```
FCMClientHelper.newInstance(applicationContext).registerPushReceivedListener(PushReceiver())
```

**Topics Messaging**

Library supports topic messaging. The app can subscribe/un-subscribe to messages whenever required.

API for subscribing to topic
 
 ```
FCMClientHelper.newInstance(applicationContext).subscribeToTopics()
```

API for un-subscribing to topic

```
FCMClientHelper.newInstance(applicationContext).unSubscribeTopic()
```

**Enable Logs**

In case you need to enable logs of the library for debugging purpose you can do so by calling the
below API. The library ensures that logs are not printed when using a signed build even if logs 
are enabled, app need not bother about disabling. 
  
```
FCMClientHelper.newInstance(applicationContext).enableLogs()
```

For some reason if enabling logs for signed build is required use the below API. Make sure this 
is removed before app is published on store.

```
FCMClientHelper.newInstance(applicationContext).enableLogsForSignedBuild()
```