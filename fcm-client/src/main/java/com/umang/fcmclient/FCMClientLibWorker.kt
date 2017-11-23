package com.umang.fcmclient

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.umang.logger.SmartLogger
import com.umang.logger.info
import com.umang.logger.verbose

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 *
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class FCMClientLibWorker : IntentService("FCMClientLibWorker"), SmartLogger {

  override fun onHandleIntent(intent: Intent?) {
    if (intent != null) {
      val action = intent.action
      when (action) {
        ACTION_REGISTER -> {
          val token: String? = FirebaseInstanceId.getInstance().token
          SharedPref.newInstance(applicationContext).pushToken = token
          if (token != null && !token.isNullOrEmpty()) {
            info("PushToken: $token")
            FCMClientHelper.newInstance(applicationContext).onTokenRegistered(token)
          }
        }
        ACTION_SUBSCRIBE -> {
          val bundleExtras = intent.extras
          if (bundleExtras != null && bundleExtras.containsKey("topics")) {
            val topics = bundleExtras.getStringArray("topics")
            for (topic in topics) {
              verbose("Subscribing to $topic")
              FirebaseMessaging.getInstance().subscribeToTopic(topic)
            }
          }
        }

        ACTION_UN_SUBSCRIBE -> {
          val bundleExtras = intent.extras
          if (bundleExtras != null && bundleExtras.containsKey(("topics"))) {
            val topics = bundleExtras.getStringArray("topics")
            for (topic in topics) {
              verbose("Un-subscribing to $topic")
              FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
            }
          }
        }
      }
    }
  }

  companion object {

    private val ACTION_REGISTER = "com.umang.fcmclient.action.REGISTER_PUSH"
    private val ACTION_SUBSCRIBE = "com.umang.fcmclient.action.SUBSCRIBE"
    private val ACTION_UN_SUBSCRIBE = "com.umang.fcmclient.action.UNSUBSCRIBE"


    fun registerForPush(context: Context) {
      val intent = Intent(context, FCMClientLibWorker::class.java)
      intent.action = ACTION_REGISTER
      context.startService(intent)
    }

    fun subscribeToTopic(context: Context, topics: List<String>) {
      val intent = Intent(context, FCMClientLibWorker::class.java)
      intent.putExtra("topics", topics.toTypedArray())
      intent.action = ACTION_SUBSCRIBE
    }

    fun unsubscribeFromTopic(context: Context, topics: List<String>){
      val intent = Intent(context, FCMClientLibWorker::class.java)
      intent.putExtra("topics", topics.toTypedArray())
      intent.action = ACTION_UN_SUBSCRIBE
    }

  }
}
