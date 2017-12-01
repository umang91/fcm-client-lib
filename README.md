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

```
FCMClientHelper.newInstance(applicationContext).initializeFCMClient(this)
```