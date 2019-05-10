package edu.washington.jonl1138.quizdroid

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_question.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [QuestionFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [QuestionFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class QuestionFragment : Fragment() {

    private var topic: String? = null
    private var questionIndex: Int? = null
    private var numCorrect: Int? = null
    private var questions: Array<Question>? = null
    private var correctAnswer: String? = null
    private var listener: OnNextClickListener? = null
    private var currentQuestion: Question? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            topic = it.getString("TOPIC")
            questionIndex = it.getInt("QUESTION_NUMBER")
            numCorrect = it.getInt("NUM_CORRECT")
        }

        val quizApp = QuizApp.instance
        val dataManager = quizApp.dataManager
        questions = dataManager.getFullTopics()[topic]!!.questions
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_question, container, false)
        questionIndex = arguments!!.getInt("QUESTION_INDEX")
        numCorrect = arguments!!.getInt("NUM_CORRECT")

        // set submit button disabled by default
        val submitButton = view.findViewById<Button>(R.id.submit)
        submitButton.setEnabled(false)

        // setting correct answer
        currentQuestion = questions!![questionIndex!!]
        correctAnswer = currentQuestion!!.answers[currentQuestion!!.correctAnswer]

        // generating question, answers, etc.
        generateContent(view)


        submitButton.setOnClickListener {
            val buttonGroup = view.findViewById<RadioGroup>(R.id.answer_group)
            val radioID = buttonGroup.checkedRadioButtonId
            val radioButton = buttonGroup.findViewById<RadioButton>(radioID)
            val userAnswer:String = radioButton.text.toString()
            if (radioButton.text.equals(correctAnswer)) {
                numCorrect = numCorrect!! + 1
            }
            questionIndex = questionIndex!! + 1
            listener!!.onNextClick(topic!!, numCorrect!!, questionIndex!!, userAnswer, correctAnswer!!)
        }
        return view
    }

    fun generateContent(view: View) {
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
            targetButton.text = currentAnswers!![i]
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
        //listener = null
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
        fun onNextClick(topic: String, numCorrect: Int, questionIndex: Int, userAnswer: String, correctAnswer: String)
    }

    companion object {
        @JvmStatic
        fun newInstance(topic: String, question_number: Int, num_correct:Int) =
            QuestionFragment().apply {
                arguments = Bundle().apply {
                    putInt("QUESTION_INDEX", question_number)
                    putInt("NUM_CORRECT", num_correct)
                    putString("TOPIC", topic)
                }
            }
    }
}
