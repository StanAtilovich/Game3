package ru.stan.game3

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment(), WinningFragment.ScoreListener {

    lateinit var rootView: View
    private var score: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)

        rootView.findViewById<Button>(R.id.rbEasy).setOnClickListener {

            findNavController().navigate(R.id.action_homeFragment_to_secondFragment)
        }

        return rootView
    }

    override fun onScoreReceived(score: Int) {
        this.score = score
        Log.d("HomeFragment", "Received score: $score") // Добавьте эту строку
        rootView.findViewById<TextView>(R.id.tvHomeScore).text = "Score: $score"
    }
}
