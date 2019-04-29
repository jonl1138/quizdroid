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

        val topics = arrayOf("Football", "Math", "Physics", "Marvel Superheroes")

        for (topic in topics) {
            Log.d("debugging", topic.toLowerCase().replace(" ","_"))
            val currID = resources.getIdentifier(topic.toLowerCase().replace(" ", "_"), "id", "edu.washington.jonl1138.quizdroid")
            Log.d("debugging", currID.toString())
            val currentButton = findViewById<Button>(currID)
            currentButton.setOnClickListener {
                val intent: Intent = Intent(baseContext, TopicActivity::class.java);
                intent.putExtra("TOPIC_NAME", topic)
                Log.d("debugging", "Reached here!")
                startActivity(intent)
            }
        }
    }
}
