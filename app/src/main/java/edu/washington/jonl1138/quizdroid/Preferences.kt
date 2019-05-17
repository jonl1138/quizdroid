package edu.washington.jonl1138.quizdroid

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class Preferences : AppCompatActivity() {

    private var alarmManager: AlarmManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        val refreshButton = findViewById<EditText>(R.id.refresh_time)
        val urlButton = findViewById<EditText>(R.id.data_url)
        val saveButton = findViewById<Button>(R.id.save)

        saveButton.setOnClickListener {
            updateURL()
            updateRefresh()
            setAlarm()
        }

        val sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE)

        urlButton.setText(sharedPreferences.getString("URL", "not accessed"))
        refreshButton.setText(sharedPreferences.getInt("REFRESH_TIME", 0).toString())

    }

    fun updateURL() {
        val urlButton = findViewById<EditText>(R.id.data_url)
        val sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("URL",urlButton.text.toString())
        editor.apply()
    }

    fun updateRefresh() {
        val refreshButton = findViewById<EditText>(R.id.refresh_time)
        val sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt("REFRESH_TIME", refreshButton.text.toString().toInt())
        editor.apply()
    }

    // calls DataBroadcastReceiver at the given user inputted times
    private fun setAlarm() {
        val sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(this, DataBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager!!.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (sharedPreferences.getInt("REFRESH_TIME", 10) * 60000).toLong(), pendingIntent)
    }
}
