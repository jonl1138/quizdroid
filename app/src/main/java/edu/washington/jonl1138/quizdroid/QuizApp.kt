package edu.washington.jonl1138.quizdroid
import android.app.Application

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