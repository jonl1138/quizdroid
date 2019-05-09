package edu.washington.jonl1138.quizdroid

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AnswerFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AnswerFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AnswerFragment : Fragment() {
    private var numCorrect: Int? = null
    private var questionIndex: Int? = null
    private var questions: Array<String>? = null
    private var correctAnswer: String? = null
    private var userAnswer: String? = null
    private var listener: OnNextAnswerListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            numCorrect = it.getInt("NUM_CORRECT")
            questionIndex = it.getInt("QUESTION_INDEX")
            questions = it.getStringArray("QUESTIONS")
            correctAnswer = it.getString("CORRECT_ANSWER")
            userAnswer = it.getString("USER_ANSWER")
        }
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

        val continueButton = view.findViewById<Button>(R.id.next)
        if (questionIndex == questions!!.size) {
            continueButton.text = "Finish"
        } else {
            continueButton.text = "Next Question"
        }
        continueButton.setOnClickListener {
            listener!!.onClick(questions!!, questionIndex!!, numCorrect!!)
        }
        return view
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnNextAnswerListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
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
        fun onClick(questions: Array<String>, questionIndex: Int, numCorrect: Int)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AnswerFragment.
         */
        @JvmStatic
        fun newInstance(numCorrect: Int,
                        questionIndex: Int,
                        questions: Array<String>,
                        userAnswer: String,
                        correctAnswer: String) =
            AnswerFragment().apply {
                arguments = Bundle().apply {
                    putInt("NUM_CORRECT", numCorrect)
                    putInt("QUESTION_INDEX", questionIndex)
                    putStringArray("QUESTIONS", questions)
                    putString("USER_ANSWER", userAnswer)
                    putString("CORRECT_ANSWER", correctAnswer)
                }
            }
    }
}
