package edu.washington.jonl1138.quizdroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // accesses topic lists from DataManager
        val quizApp = QuizApp.instance
        val dataManager = quizApp.dataManager
        val topics = dataManager.getFullTopics()!!
        Log.d("debugging", topics.keys.toString())
        for (topic in topics.keys) {

            val currID = resources.getIdentifier(topic.toLowerCase().replace(" ", "_"), "id", "edu.washington.jonl1138.quizdroid")
            val currDescID = resources.getIdentifier(topic.toLowerCase().replace(" ", "_") + "_desc", "id", "edu.washington.jonl1138.quizdroid")
            val currentButton = findViewById<Button>(currID)
            val currentDesc = findViewById<TextView>(currDescID)
            currentDesc.text = topics[topic]!!.shortDesc
            currentButton.setOnClickListener {
                val intent: Intent = Intent(baseContext, TopicActivity::class.java);
                intent.putExtra("TOPIC_NAME", topic)
                startActivity(intent)
            }
        }
    }
}
