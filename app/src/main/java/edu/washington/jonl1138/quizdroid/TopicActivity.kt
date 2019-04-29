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

class TopicActivity : AppCompatActivity() {

    val descriptions: Map<String, String> = mapOf("Football" to "All Questions about American Football",
        "Math" to "All questions about Math",
        "Physics" to "All questions about Physics",
        "Marvel Superheroes" to "All questions about Marvel Superheroes"
    )
    val questions: Map<String, Array<String>> = mapOf("Football" to arrayOf<String>("Who is the quarterback for the New England Patriots?",
        "Who was drafted #1 overall in the 2019 NFL draft?",
        "What position did Marshawn Lynch play?"),
        "Math" to arrayOf<String>("What is 4+4?"),
        "Physics" to arrayOf<String>("What is equal to mass * acceleration?"),
        "Marvel Superheroes" to arrayOf<String>("What is Captain America's shield made of?"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic)
        Log.d("debugging", "Reached here!2")
        val intent: Intent = getIntent()
        val category: String = intent.getStringExtra("TOPIC_NAME")

        // updates displayed title
        val currentTitle = findViewById<TextView>(R.id.title)
        currentTitle.text = category

        // updates displayed description
        val currentDesc = findViewById<TextView>(R.id.description)
        currentDesc.text = descriptions.get(category)

        // updates displayed # questions
        val count: Int = questions.get(category)!!.size
        question_count.text = "Question Count: " + count.toString()

        // sets onClickListener
        val beginButton = findViewById<Button>(R.id.begin)
        beginButton.setOnClickListener {
            val intent: Intent = Intent(baseContext, QuestionActivity::class.java)

            intent.putExtra("QUESTIONS", questions.get(category))
            intent.putExtra("QUESTION_NUMBER", 0)
            intent.putExtra("NUM_CORRECT", 0)
            Log.d("debugging", category)
            startActivity(intent)
        }
    }
}
