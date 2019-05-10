package edu.washington.jonl1138.quizdroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_topic.*
import org.w3c.dom.Text
import java.io.Serializable

class TopicActivity : AppCompatActivity(), TopicFragment.BeginClickListener, QuestionFragment.OnNextClickListener, AnswerFragment.OnNextAnswerListener {

    private var questionIndex: Int? = null
    private var currentTopic: String? = null
    private var questions: Array<Question>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic)

        // grabs inputted topic name
        val intent: Intent = getIntent()
        currentTopic = intent.getStringExtra("TOPIC_NAME")

        // accesses DataManager to get stored descriptions/ questions lists
        val quizApp = QuizApp.instance
        val dataManager = quizApp.dataManager
        questions = dataManager.getFullTopics()[currentTopic!!]!!.questions

        // By default starts TopicFragment
        val topicDescFragment = TopicFragment.newInstance(currentTopic!!)
        supportFragmentManager.beginTransaction().run {
            add(R.id.fragment_container, topicDescFragment, "TOPIC_FRAGMENT")
            commit()
        }
    }

    override fun onClick(topic: String, questionIndex: Int, numCorrect: Int) {
        if (questionIndex == questions!!.size) {
            val intent: Intent = Intent(baseContext, MainActivity::class.java);
            startActivity(intent)

        } else {
            Log.d("debugging", questionIndex.toString())
            val questionFragment = QuestionFragment.newInstance(currentTopic!!, questionIndex, numCorrect)
            supportFragmentManager.beginTransaction().run {
                replace(R.id.fragment_container, questionFragment, "QUESTION_FRAGMENT")
                commit()
            }
        }
    }

    override fun onNextClick(
        topic: String,
        numCorrect: Int,
        questionIndex: Int,
        userAnswer: String,
        correctAnswer: String
    ) {
        val answerFragment = AnswerFragment.newInstance(currentTopic!!, numCorrect, questionIndex, userAnswer, correctAnswer)
        supportFragmentManager.beginTransaction().run {
            replace(R.id.fragment_container, answerFragment, "ANSWER_FRAGMENT")
            commit()

        }
    }

    override fun onBeginClick(question_number: Int, topic: String) {
        questionIndex = question_number
        currentTopic = topic
        val questionFragment = QuestionFragment.newInstance(currentTopic!!, 0, 0)
        supportFragmentManager.beginTransaction().run {
            replace(R.id.fragment_container, questionFragment, "QUESTION_FRAGMENT")
            commit()
        }
    }




}
