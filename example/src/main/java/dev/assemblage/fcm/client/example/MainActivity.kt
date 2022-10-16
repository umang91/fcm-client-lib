package dev.assemblage.fcm.client.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.assemblage.fcm.client.FcmClientHelper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val topics = ArrayList<String>()
        topics.add("topic1")
        topics.add("topic2")
        topics.add("topic3")
        FcmClientHelper.getInstance(applicationContext).subscribeToTopics(topics)
    }
}
