package edu.washington.jonl1138.quizdroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

// Activity that serves as hub for every fragment
class TopicActivity : AppCompatActivity(), TopicFragment.BeginClickListener, QuestionFragment.OnNextClickListener, AnswerFragment.OnNextAnswerListener {

    private var questionIndex: Int? = null
    private var currentTopic: Int? = null
    private var questions: Array<Question>? = null
    private var currentFullTopic: Topic? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic)

        // grabs inputted topic name
        val intent = getIntent()
        currentTopic = intent.getIntExtra("TOPIC_INDEX", 0)

        // accesses DataManager to get stored descriptions/ questions lists
        val quizApp = QuizApp.instance
        val dataManager = quizApp.dataManager
        questions = dataManager.getFullTopics()[currentTopic!!].questions
        currentFullTopic = dataManager.getFullTopics()[currentTopic!!]
        // By default starts TopicFragment
        val topicDescFragment = TopicFragment.newInstance(currentTopic!!)
        supportFragmentManager.beginTransaction().run {
            add(R.id.fragment_container, topicDescFragment, "TOPIC_FRAGMENT")
            commit()
        }
    }

    // overwrites AnswerFragment's onClick function
    // either displays another QuestionFragment or to the main menu depending on whether questions still left
    override fun onClick(topicIndex: Int, questionIndex: Int, numCorrect: Int) {
        if (questionIndex == questions!!.size) {
            val intent = Intent(baseContext, MainActivity::class.java)
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

    // overwrites QuestionFragment's onNextClick function
    // advances to an Answer Fragment
    override fun onNextClick(
        topicIndex: Int,
        numCorrect: Int,
        questionIndex: Int,
        userAnswer: String,
        correctAnswer: String
    ) {
        val answerFragment = AnswerFragment.newInstance(topicIndex, numCorrect, questionIndex, userAnswer, correctAnswer)
        supportFragmentManager.beginTransaction().run {
            replace(R.id.fragment_container, answerFragment, "ANSWER_FRAGMENT")
            commit()

        }
    }

    // overwrites TopicFragment's onBeginClick function
    // advances to the first QuestionFragment
    override fun onBeginClick(question_number: Int, topic_index: Int) {
        questionIndex = question_number
        currentTopic = topic_index
        val questionFragment = QuestionFragment.newInstance(currentTopic!!, 0, 0)
        supportFragmentManager.beginTransaction().run {
            replace(R.id.fragment_container, questionFragment, "QUESTION_FRAGMENT")
            commit()
        }
    }




}
