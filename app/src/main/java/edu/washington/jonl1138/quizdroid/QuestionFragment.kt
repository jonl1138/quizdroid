package edu.washington.jonl1138.quizdroid

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView

// Fragment representing "question" portion of QuizDroid
class QuestionFragment : Fragment() {
    private var topicIndex: Int? = null
    private var questionIndex: Int? = null
    private var numCorrect: Int? = null
    private var questions: Array<Question>? = null
    private var correctAnswer: String? = null
    private var listener: OnNextClickListener? = null
    private var currentQuestion: Question? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            topicIndex = it.getInt("TOPIC_INDEX")
            questionIndex = it.getInt("QUESTION_INDEX")
            numCorrect = it.getInt("NUM_CORRECT")
        }

        val quizApp = QuizApp.instance
        val dataManager = quizApp.dataManager
        questions = dataManager.getFullTopics()[topicIndex!!].questions
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_question, container, false)

        // set submit button disabled by default
        val submitButton = view.findViewById<Button>(R.id.submit)
        submitButton.setEnabled(false)

        // setting correct answer
        currentQuestion = questions!![questionIndex!!]
        correctAnswer = currentQuestion!!.answers[currentQuestion!!.correctAnswer - 1]

        // generating question, answers, etc.
        generateContent(view)

        // sets the submit button to move to the Answer fragment
        submitButton.setOnClickListener {
            val buttonGroup = view.findViewById<RadioGroup>(R.id.answer_group)
            val radioID = buttonGroup.checkedRadioButtonId
            val radioButton = buttonGroup.findViewById<RadioButton>(radioID)
            val userAnswer:String = radioButton.text.toString()
            if (radioButton.text.equals(correctAnswer)) {
                numCorrect = numCorrect!! + 1
            }
            questionIndex = questionIndex!! + 1
            listener!!.onNextClick(topicIndex!!, numCorrect!!, questionIndex!!, userAnswer, correctAnswer!!)
        }
        return view
    }

    private fun generateContent(view: View) {
        // generates buttons
        val currentQ = currentQuestion!!.question
        val currentAnswers = currentQuestion!!.answers

        // generate question
        val currentTitle = view.findViewById<TextView>(R.id.question)
        currentTitle.text = currentQ

        var takenOptions: Set<Int> = setOf(5)
        // loop over answers to current question
        for (i in 0..3) {
            var rndIndex = 5
            while (takenOptions.contains(rndIndex)) {
                rndIndex = (1..4).random()
            }
            takenOptions = takenOptions.plus(rndIndex)
            val targetID = resources.getIdentifier("answer" + rndIndex, "id", "edu.washington.jonl1138.quizdroid")
            val targetButton = view.findViewById<RadioButton>(targetID)
            targetButton.text = currentAnswers[i]

            // activate submit button if any button is selected
            targetButton.setOnClickListener {
                view.findViewById<Button>(R.id.submit).setEnabled(true)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnNextClickListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnNextClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnNextClickListener {
        fun onNextClick(topicIndex: Int, numCorrect: Int, questionIndex: Int, userAnswer: String, correctAnswer: String)
    }

    companion object {
        @JvmStatic
        fun newInstance(topicIndex: Int, questionNumber: Int, numCorrect:Int) =
            QuestionFragment().apply {
                arguments = Bundle().apply {
                    putInt("QUESTION_INDEX", questionNumber)
                    putInt("NUM_CORRECT", numCorrect)
                    putInt("TOPIC_INDEX", topicIndex)
                }
            }
    }
}
