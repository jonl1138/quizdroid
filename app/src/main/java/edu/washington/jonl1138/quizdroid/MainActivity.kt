package edu.washington.jonl1138.quizdroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // accesses topic lists from DataManager
        val quizApp = QuizApp.instance
        val dataManager = quizApp.dataManager
        val topics = dataManager.getTopics()!!

        for (topic in topics) {
            val currID = resources.getIdentifier(topic.toLowerCase().replace(" ", "_"), "id", "edu.washington.jonl1138.quizdroid")
            val currentButton = findViewById<Button>(currID)
            currentButton.setOnClickListener {
                val intent: Intent = Intent(baseContext, TopicActivity::class.java);
                intent.putExtra("TOPIC_NAME", topic)
                startActivity(intent)
            }
        }
    }
}
