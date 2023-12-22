package ru.stan.game3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class WinningFragment : Fragment() {
    private lateinit var tvScore: TextView
    private var scoreListener: ScoreListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_winning, container, false)
        tvScore = view.findViewById(R.id.tvScore)
        val score = arguments?.getInt("score") ?: 0
        tvScore.text = "Score: $score"
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnBack).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
    fun setScoreListener(listener: ScoreListener) {
        scoreListener = listener
    }
    private fun sendScoreBack() {
        val score = arguments?.getInt("score") ?: 0
        scoreListener?.onScoreReceived(score)
    }
    interface ScoreListener {
        fun onScoreReceived(score: Int)
    }


}