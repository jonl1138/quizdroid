package edu.washington.jonl1138.quizdroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class AnswerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

        var intent: Intent = getIntent()

        // pass in answers
        val numCorrect = intent.getIntExtra("NUM_CORRECT", 5000)
        val questionIndex = intent.getIntExtra("QUESTION_NUMBER", 5000)
        val questions = intent.getStringArrayExtra("QUESTIONS")
        val userAnswer = intent.getStringExtra("USER_ANSWER")
        val correctAnswer = intent.getStringExtra("CORRECT_ANSWER")

        findViewById<TextView>(R.id.correctCount).text = "# of Correct Answers:" + numCorrect.toString() + "/" + questions.size
        findViewById<TextView>(R.id.userAnswer).text = "Your Answer:" + userAnswer
        findViewById<TextView>(R.id.correctAnswer).text = "Correct Answer:" + correctAnswer

        val continueButton = findViewById<Button>(R.id.next)
        if (questionIndex == questions.size) {
            continueButton.text = "Finish"
            continueButton.setOnClickListener {
                val intent: Intent = Intent(baseContext, MainActivity::class.java)
                startActivity(intent)
            }
        } else {
            continueButton.text = "Next Question"
            continueButton.setOnClickListener {
                val intent: Intent = Intent(baseContext, QuestionActivity::class.java)
                intent.putExtra("QUESTIONS", questions)
                intent.putExtra("QUESTION_NUMBER", questionIndex)
                intent.putExtra("NUM_CORRECT", numCorrect)
                startActivity(intent)
            }
        }


    }
}
