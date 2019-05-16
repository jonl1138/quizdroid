package edu.washington.jonl1138.quizdroid

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class TopicFragment : Fragment() {
    private var category: Int? = null
    private var description: String? = null
    private var title: String? = null
    private var questionCount: Int? = null
    private var beginListener: BeginClickListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val quizApp = QuizApp.instance
        val dataManager = quizApp.dataManager
        val topics = dataManager.getFullTopics()
        category = arguments!!.getInt("TOPIC_INDEX")
        description = topics[category!!].desc
        title = topics[category!!].title
        questionCount = topics[category!!].questions.size
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View =  inflater.inflate(R.layout.fragment_topic, container, false)

        // updates displayed title
        val currentTitle = view.findViewById<TextView>(R.id.title)
        currentTitle.text = title

        // updates displayed description
        val currentDesc = view.findViewById<TextView>(R.id.description)
        currentDesc.text = description

        // updates displayed # questions
        val questionCountView = view.findViewById<TextView>(R.id.question_count)
        questionCountView.text = "Question Count: " + questionCount.toString()

        // sets onClickListener
        val beginButton = view.findViewById<Button>(R.id.begin)
        beginButton.setOnClickListener {
            beginListener!!.onBeginClick(0, category!!)

        }
        return view
    }

    // interface for TopicActivity to implement
    interface BeginClickListener {
        fun onBeginClick(question_number: Int, topic_index: Int)
    }

    override fun onDetach() {
        super.onDetach()
        beginListener = null
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.d("debugging", "Attached!")
        beginListener = context as BeginClickListener
        if(beginListener == null){
            throw ClassCastException("$context must implement onBeginClick")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(topic: Int) =
            TopicFragment().apply {
                arguments = Bundle().apply {
                    putInt("TOPIC_INDEX", topic)
                }
            }

    }
}
