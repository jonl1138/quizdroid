package edu.washington.jonl1138.quizdroid

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.widget.Button

import kotlinx.android.synthetic.main.activity_airplane_redirect.*

class AirplaneRedirect : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airplane_redirect)

        val yes = findViewById<Button>(R.id.yes)
        yes.setOnClickListener {
            startActivityForResult(Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS), 1);
        }

        val no = findViewById<Button>(R.id.no)
        no.setOnClickListener {
            val noIntent = Intent(baseContext, MainActivity::class.java)
            noIntent.putExtra("REDIRECTED", true)
            startActivity(noIntent)
        }
    }

}
