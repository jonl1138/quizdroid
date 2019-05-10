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

    private var questionIndex = 0
    private var currentTopic = ""
    lateinit private var descriptions: Map<String, String>
    lateinit private var questions: Map<String, Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic)

        // grabs inputted topic name
        val intent: Intent = getIntent()
        currentTopic = intent.getStringExtra("TOPIC_NAME")

        // accesses DataManager to get stored descriptions/ questions lists
        val quizApp = QuizApp.instance
        val dataManager = quizApp.dataManager
        descriptions = dataManager.getDescriptions()
        questions = dataManager.getQuestions()

        // By default starts TopicFragment
        val topicDescFragment = TopicFragment.newInstance(currentTopic, descriptions.get(currentTopic)!!, questions.get(currentTopic)!!.size)
        supportFragmentManager.beginTransaction().run {
            add(R.id.fragment_container, topicDescFragment, "TOPIC_FRAGMENT")
            commit()
        }
    }

    override fun onClick(input_questions: Array<String>, questionIndex: Int, numCorrect: Int) {
        if (questionIndex == input_questions.size) {
            val intent: Intent = Intent(baseContext, MainActivity::class.java);
            startActivity(intent)

        } else {
            val questionFragment = QuestionFragment.newInstance(questions[currentTopic]!!, questionIndex, numCorrect)
            supportFragmentManager.beginTransaction().run {
                replace(R.id.fragment_container, questionFragment, "QUESTION_FRAGMENT")
                commit()
            }
        }
    }

    override fun onNextClick(
        numCorrect: Int,
        questionIndex: Int,
        questions: Array<String>,
        userAnswer: String,
        correctAnswer: String
    ) {
        val answerFragment = AnswerFragment.newInstance(numCorrect, questionIndex, questions, userAnswer, correctAnswer)
        supportFragmentManager.beginTransaction().run {
            replace(R.id.fragment_container, answerFragment, "ANSWER_FRAGMENT")
            commit()

        }
    }

    override fun onBeginClick(question_number: Int, topic: String) {
        questionIndex = question_number
        currentTopic = topic
        val questionFragment = QuestionFragment.newInstance(questions[currentTopic]!!, 0, 0)
        supportFragmentManager.beginTransaction().run {
            replace(R.id.fragment_container, questionFragment, "QUESTION_FRAGMENT")
            commit()
        }
    }




}
