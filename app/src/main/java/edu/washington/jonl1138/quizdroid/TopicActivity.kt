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

class TopicActivity : AppCompatActivity(), TopicFragment.BeginClickListener, QuestionFragment.NextClickListener, AnswerFragment.OnNextAnswerListener {

    private var questionIndex = 0
    private var currentTopic = ""
    private val descriptions: Map<String, String> = mapOf("Football" to "All Questions about American Football",
        "Math" to "All questions about Math",
        "Physics" to "All questions about Physics",
        "Marvel Superheroes" to "All questions about Marvel Superheroes"
    )
    private val questions: Map<String, Array<String>> = mapOf("Football" to arrayOf<String>("Who is the quarterback for the New England Patriots?",
        "Who was drafted #1 overall in the 2019 NFL draft?",
        "What position did Marshawn Lynch play?"),
        "Math" to arrayOf<String>("What is 4+4?"),
        "Physics" to arrayOf<String>("What is equal to mass * acceleration?"),
        "Marvel Superheroes" to arrayOf<String>("What is Captain America's shield made of?"))

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic)

        val intent: Intent = getIntent()
        currentTopic = intent.getStringExtra("TOPIC_NAME")

        val topicDescFragment = TopicFragment.newInstance(currentTopic, descriptions.get(currentTopic)!!, questions.get(currentTopic)!!.size)
        supportFragmentManager.beginTransaction().run {
            add(R.id.fragment_container, topicDescFragment, "TOPIC_FRAGMENT")
            commit()
        }
    }

}
