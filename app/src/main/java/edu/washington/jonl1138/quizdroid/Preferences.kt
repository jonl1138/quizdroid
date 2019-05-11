package edu.washington.jonl1138.quizdroid

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

class Preferences : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        val refreshButton = findViewById<EditText>(R.id.refresh_time)
        val urlButton = findViewById<EditText>(R.id.data_url)
        val saveButton = findViewById<Button>(R.id.save)

        saveButton.setOnClickListener {
            updateURL(urlButton.text.toString())
            updateRefresh(refreshButton.text.toString().toInt())
        }

        val sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE)

        urlButton.setText(sharedPreferences.getString("URL", "not accessed"))
        refreshButton.setText(sharedPreferences.getInt("REFRESH_TIME", 0).toString())
    }

    fun updateURL(newURL: String) {
        val urlButton = findViewById<EditText>(R.id.data_url)
        val sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("URL",urlButton.text.toString())
        editor.commit()
    }

    fun updateRefresh(newRefresh: Int) {
        val refreshButton = findViewById<EditText>(R.id.refresh_time)
        val sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt("REFRESH_TIME", refreshButton.text.toString().toInt())
        editor.commit()
    }
}
