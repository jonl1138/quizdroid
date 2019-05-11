package edu.washington.jonl1138.quizdroid

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setSupportActionBar(findViewById(R.id.my_toolbar))
        getSupportActionBar()!!.setTitle("QuizDroid")

        // initializes default values in shared preferences
        val sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("URL","http://tednewardsandbox.site44.com/questions.json")
        editor.putInt("REFRESH_TIME", 10)
        editor.apply()

        // accesses topic lists from DataManager
        val quizApp = QuizApp.instance
        val dataManager = quizApp.dataManager
        val topics = dataManager.getFullTopics()
        Log.d("debugging", "Loaded array of topics")
        for (i in 1..topics.size) {
            val currID = resources.getIdentifier(
                "topic" + i.toString(),
                "id",
                "edu.washington.jonl1138.quizdroid"
            )
            val currDescID = resources.getIdentifier(
                "topic" + i.toString() + "_desc",
                "id",
                "edu.washington.jonl1138.quizdroid"
            )
            Log.d("debugging", "Loaded array of topics")
            val currentButton = findViewById<Button>(currID)
            val currentDesc = findViewById<TextView>(currDescID)
            currentButton.text = topics[i-1].title
            currentDesc.text = topics[i-1].desc
            currentButton.setOnClickListener {
                val intent = Intent(baseContext, TopicActivity::class.java)
                intent.putExtra("TOPIC_INDEX", i-1)
                Log.d("debugging", (i-1).toString())
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        Log.d("debugging", "created options menu")
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val currentID = item!!.itemId
        if (currentID == R.id.preferences) {
            Log.d("debugging", "clicked preferences!")
            val intent = Intent(baseContext, Preferences::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)

    }
}