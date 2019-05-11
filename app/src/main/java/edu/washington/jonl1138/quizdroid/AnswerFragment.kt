package edu.washington.jonl1138.quizdroid

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class AnswerFragment : Fragment() {
    private var topicIndex: Int? = null
    private var numCorrect: Int? = null
    private var questionIndex: Int? = null
    private var questions: Array<Question>? = null
    private var correctAnswer: String? = null
    private var userAnswer: String? = null
    private var listener: OnNextAnswerListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            numCorrect = it.getInt("NUM_CORRECT")
            questionIndex = it.getInt("QUESTION_INDEX")
            correctAnswer = it.getString("CORRECT_ANSWER")
            userAnswer = it.getString("USER_ANSWER")
            topicIndex = it.getInt("TOPIC_INDEX")
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
        val view = inflater.inflate(R.layout.fragment_answer, container, false)
        view.findViewById<TextView>(R.id.correctCount).text = "# of Correct Answers:" + numCorrect.toString() + "/" + questions!!.size
        view.findViewById<TextView>(R.id.userAnswer).text = "Your Answer:" + userAnswer
        view.findViewById<TextView>(R.id.correctAnswer).text = "Correct Answer:" + correctAnswer

        // determine if button should display "Finish" or "Next Question"
        val continueButton = view.findViewById<Button>(R.id.next)
        if (questionIndex == questions!!.size) {
            continueButton.text = "Finish"
        } else {
            continueButton.text = "Next Question"
        }
        continueButton.setOnClickListener {
            listener!!.onClick(topicIndex!!, questionIndex!!, numCorrect!!)
        }
        return view
    }


    // as Fragment is attached, assign listener to TopicActivity's context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnNextAnswerListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnNextAnswerListener")
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
    interface OnNextAnswerListener {
        fun onClick(topicIndex: Int, questionIndex: Int, numCorrect: Int)
    }

    // companion object that initializes an Answer Fragment
    companion object {
        @JvmStatic
        fun newInstance(topicIndex: Int,
                        numCorrect: Int,
                        questionIndex: Int,
                        userAnswer: String,
                        correctAnswer: String) =
            AnswerFragment().apply {
                arguments = Bundle().apply {
                    putInt("NUM_CORRECT", numCorrect)
                    putInt("QUESTION_INDEX", questionIndex)
                    putString("USER_ANSWER", userAnswer)
                    putString("CORRECT_ANSWER", correctAnswer)
                    putInt("TOPIC_INDEX", topicIndex)
                }
            }
    }
}
