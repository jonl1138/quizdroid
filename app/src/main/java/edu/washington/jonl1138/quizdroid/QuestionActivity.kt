package edu.washington.jonl1138.quizdroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView

class QuestionActivity : AppCompatActivity() {
    var correctAnswer: String = ""
    val answers = mapOf("Who is the quarterback for the New England Patriots?" to arrayOf<String>("Tom Brady", "Drew Bledsoe", "Peyton Manning", "Eli Manning"),
        "Who was drafted #1 overall in the 2019 NFL draft?" to arrayOf<String>("Kyler Murray", "Nick Bosa", "Josh Allen", "Dwayne Haskins"),
        "What position did Marshawn Lynch play?" to arrayOf<String>("Running back", "Wide Receiver", "Tight End", "Linebacker"),
        "What is 4+4?" to arrayOf<String>("8", "10", "13", "2"),
        "What is equal to mass * acceleration?" to arrayOf<String>("Force", "Momentum", "Gravity", "Energy"),
        "What is Captain America's shield made of?" to arrayOf<String>("Vibranium", "Plutonium", "Steel", "Bronze"))

    var questionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)


        // import in topic name and questions array
        val intent = getIntent()

        val questions = intent.getStringArrayExtra("QUESTIONS")
        questionIndex = intent.getIntExtra("QUESTION_NUMBER", 5000)
        var numCorrect = intent.getIntExtra("NUM_CORRECT", 5000)


        // setting submit button disabled by default
        val submitButton = findViewById<Button>(R.id.submit)
        submitButton.setEnabled(false)

        // setting correct answer
        val currentQuestion = questions[questionIndex]
        correctAnswer = answers.get(currentQuestion)!![0]



        // generate question content
        generateContent(questions)


        // setting submit activity
        val buttonGroup = findViewById<RadioGroup>(R.id.answer_group)
        submitButton.setOnClickListener {
            val radioID = buttonGroup.checkedRadioButtonId
            val radioButton = buttonGroup.findViewById<RadioButton>(radioID)

            if (radioButton.text.equals(correctAnswer)) {
                Log.d("debugging", "clicked correct answer!")
                numCorrect += 1


            }
            questionIndex++
            val userAnswer:String = radioButton.text.toString()
            val intent: Intent = Intent(baseContext, AnswerActivity::class.java)
            intent.putExtra("NUM_CORRECT", numCorrect)

            intent.putExtra("QUESTION_NUMBER", questionIndex)
            intent.putExtra("QUESTIONS", questions)
            intent.putExtra("USER_ANSWER", userAnswer)
            intent.putExtra("CORRECT_ANSWER", correctAnswer)
            startActivity(intent)
        }

    }

    fun generateContent(questions: Array<String>) {
        // generates buttons
        val currentQuestion = questions[questionIndex]
        val currentAnswers = answers.get(currentQuestion)

        // generate question
        val currentTitle = findViewById<TextView>(R.id.question)
        currentTitle.text = currentQuestion

        var takenOptions: Set<Int> = setOf(5)
        // loop over answers to current question
        for (i in 0..3) {
            var rndIndex = 5
            while (takenOptions.contains(rndIndex)) {
                rndIndex = (1..4).random()
            }
            takenOptions = takenOptions.plus(rndIndex)
            val targetID = resources.getIdentifier("answer" + rndIndex, "id", "edu.washington.jonl1138.quizdroid")
            val targetButton = findViewById<RadioButton>(targetID)
            targetButton.text = currentAnswers!![i]
            // activate submit button if any button is selected
            targetButton.setOnClickListener {
                findViewById<Button>(R.id.submit).setEnabled(true)
            }
        }
    }
}
