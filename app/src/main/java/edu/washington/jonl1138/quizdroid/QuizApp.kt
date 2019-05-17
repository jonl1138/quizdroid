package edu.washington.jonl1138.quizdroid
import android.app.Application
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.util.Log
import android.widget.Toast
import java.lang.Exception
import java.net.InetAddress



// Application Manager
class QuizApp: Application() {

    lateinit var dataManager: TopicRepository

    companion object {
        lateinit var instance: QuizApp
    }

    override fun onCreate() {
        super.onCreate()



        instance = this
        dataManager = TopicRepository(this)

    }

}

