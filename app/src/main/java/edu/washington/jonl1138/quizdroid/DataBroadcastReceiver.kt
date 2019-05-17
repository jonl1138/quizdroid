package edu.washington.jonl1138.quizdroid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.io.IOException
import java.io.OutputStreamWriter

class DataBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val sharedPreferences = context!!.getSharedPreferences("Preferences", Context.MODE_PRIVATE)

        // Data download
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(context)
        val url = sharedPreferences.getString("URL", "")

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                saveFile(context, response)
            },
            Response.ErrorListener { Log.d("debugging","That didn't work!") })
        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    private fun saveFile(context: Context, mytext: String): Boolean {
        try {
            val fos = context.openFileOutput("questions.json", Context.MODE_PRIVATE)
            val out = OutputStreamWriter(fos)
            out.write(mytext)
            out.close()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

    }
}