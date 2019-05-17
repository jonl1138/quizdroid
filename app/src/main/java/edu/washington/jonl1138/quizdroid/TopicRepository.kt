package edu.washington.jonl1138.quizdroid

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.BufferedReader
import java.io.InputStreamReader

import java.util.*


class TopicRepository(context: Context) {
    private val context = context
    private var Topics: ArrayList<Topic> = ArrayList<Topic>()

    init {
        updateData()
    }


    fun getFullTopics(): ArrayList<Topic> {
        return Topics
    }

    // searches for data stored in file "questions.json"
    private fun updateData() {
        val fis = context.openFileInput("questions.json")
        val isr = InputStreamReader(fis)

        val bufferedReader = BufferedReader(isr)
        val sb = StringBuilder()
        var line: String?

        while ((bufferedReader.readLine().also { line = it }) != null ) {
            sb.append(line)
        }

        val jsonString: String = sb.toString()
        val gson = Gson()
        val test: Array<Topic> = gson.fromJson(jsonString, Array<Topic>::class.java)
        for (member in test) {
            Topics.add(member)
        }

    }

    // function that determines if the file with the given filename exists in the /data folder
    fun fileExists(context: Context, filename: String): Boolean {
        val file = context.getFileStreamPath(filename)
        return if (file == null || !file.exists()) {
            false
        } else true
    }

}
data class Question(@SerializedName("text") val question: String, val answers: Array<String>, @SerializedName("answer") val correctAnswer: Int)

data class Topic(val title: String, val desc: String, val questions: Array<Question>)