package com.umang.example.fcmclient

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.umang.fcmclient.FCMClientHelper

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val topics = ArrayList<String>()
    topics.add("topic1")
    topics.add("topic2")
    topics.add("topic3")
    FCMClientHelper.newInstance(applicationContext).subscribeToTopics(topics)
  }
}
