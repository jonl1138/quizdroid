package edu.washington.jonl1138.quizdroid

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.lang.Exception
import java.net.InetAddress

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // tells activity whether user has been redirected here or if it is a first visit
        val intent = getIntent()
        val isRedirected = intent.getBooleanExtra("REDIRECTED", false)

        // handles if android device is connected to internet
        var isAirplane = false
        Thread(Runnable {
            isAirplane = isAirplaneModeOn()
            try {
                InetAddress.getByName("www.google.com").isReachable(3)
            } catch (e: Exception) {
                showToast("Not connected to internet!")
            }
            if (isAirplane && !isRedirected){
                val airplaneIntent = Intent(baseContext, AirplaneRedirect::class.java)
                startActivity(airplaneIntent)
            } else if (!isAirplane){
                // only starts the data update cycle if internet is connected
                setAlarm()
                loadData()
            }
        }).start()

        // Set taskbar title
        setSupportActionBar(findViewById(R.id.my_toolbar))
        getSupportActionBar()!!.setTitle("QuizDroid")


        // initializes default values in shared preferences
        val sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("URL","http://tednewardsandbox.site44.com/questions.json")
        editor.putInt("REFRESH_TIME", 10)
        editor.apply()

    }

    // creates options menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    // assigns listener to toolbar items (Preferences button)
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val currentID = item!!.itemId
        if (currentID == R.id.preferences) {
            val intent = Intent(baseContext, Preferences::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)

    }

    // helper function to display Toasts inside other threads
    private fun showToast(toast: String) {
        runOnUiThread { Toast.makeText(this, toast, Toast.LENGTH_SHORT).show() }
    }

    // function to test if device is connected to internet
    private fun isAirplaneModeOn(): Boolean {
        return Settings.System.getInt(
            this.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0
        ) !== 0

    }

    // function that calls DataBroadcastReceiver to start download data cycle
    private fun setAlarm() {
        val sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(this, DataBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (sharedPreferences.getInt("REFRESH_TIME", 10) * 60000).toLong(), pendingIntent)
    }

    private fun loadData() {
        // accesses topic lists from central DataManager repository
        val quizApp = QuizApp.instance
        val dataManager = quizApp.dataManager
        val topics = dataManager.getFullTopics()

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
            // loads topics
            val currentButton = findViewById<Button>(currID)
            val currentDesc = findViewById<TextView>(currDescID)
            currentButton.text = topics[i - 1].title
            currentDesc.text = topics[i - 1].desc
            currentButton.setOnClickListener {
                val buttonIntent = Intent(baseContext, TopicActivity::class.java)
                buttonIntent.putExtra("TOPIC_INDEX", i - 1)
                startActivity(buttonIntent)
            }
        }
    }
}