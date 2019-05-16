package edu.washington.jonl1138.quizdroid

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class TopicRepository(context: Context) {

    private val context = context
    private var Topics: ArrayList<Topic> = ArrayList<Topic>()

    init {
        updateData()
    }

    fun getFullTopics(): ArrayList<Topic> {
        return Topics
    }

    private fun updateData() {
        val sharedPreferences = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val jsonString = context.assets.open("questions.json").bufferedReader().use{
            it.readText()
        }
        val gson = Gson()
        Log.d("debugging", jsonString)
        val test: Array<Topic> = gson.fromJson(jsonString, Array<Topic>::class.java)
        for (member in test) {
            Log.d("debugging", member.toString())
            Topics.add(member)
        }

    }
}

data class Question(@SerializedName("text") val question: String, val answers: Array<String>, @SerializedName("answer") val correctAnswer: Int)

data class Topic(val title: String, val desc: String, val questions: Array<Question>)