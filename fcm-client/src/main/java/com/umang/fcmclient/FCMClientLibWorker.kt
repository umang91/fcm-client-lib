package com.umang.fcmclient

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessaging
import com.umang.logger.SmartLogger
import com.umang.logger.verbose

/**
 * @author Umang Chamaria
 */
class FCMClientLibWorker : IntentService("FCMClientLibWorker"), SmartLogger {

  override fun onHandleIntent(intent: Intent?) {
    if (intent != null) {
      val action = intent.action
      when (action) {
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

    private val ACTION_SUBSCRIBE = "com.umang.fcmclient.action.SUBSCRIBE"
    private val ACTION_UN_SUBSCRIBE = "com.umang.fcmclient.action.UNSUBSCRIBE"


    fun subscribeToTopic(context: Context, topics: List<String>) {
      val intent = Intent(context, FCMClientLibWorker::class.java)
      intent.putExtra("topics", topics.toTypedArray())
      intent.action = ACTION_SUBSCRIBE
      context.startService(intent)
    }

    fun unsubscribeFromTopic(context: Context, topics: List<String>){
      val intent = Intent(context, FCMClientLibWorker::class.java)
      intent.putExtra("topics", topics.toTypedArray())
      intent.action = ACTION_UN_SUBSCRIBE
      context.startService(intent)
    }

  }
}
