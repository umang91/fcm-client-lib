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


# Usage

**Initialisation**

Initialise the FCM client SDK in the `onCreate()` of the Application subclass. To initialise add 
the below line.

```
FCMClientHelper.newInstance(applicationContext).initializeFCMClient(this)
```

**Get push token**

To get push token from the  library you need to register for a callback listener. It is 
recommended to register for the listener in the `onCreate()` of the application class. If the 
listener is set in other places such as activity/fragment token refresh callbacks might be missed.

**Implement listener**

To get a callback your class should implement the `FCMClientHelper.TokenReceivedListener` 
provided by the library.

Below is a sample implementation 

```
class TokenReceiver:FCMClientHelper.TokenReceivedListener {
  override fun onTokenReceived(token: String) {
    Log.v("SampleApplication", "TokenReceiver : $token")
    //save token or send to server or pass to other sdks
  }
}

```

**Register listener**

Below is an example of setting a token receiver in the `onCreate()` of Application class

```
FCMClientHelper.newInstance(applicationContext).registerTokenRegistrationListener(TokenReceiver())
```