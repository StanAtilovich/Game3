package ru.stan.game3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {
    lateinit var rootView: View
    var oldTvScore2Value: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)

        rootView.findViewById<Button>(R.id.rbEasy).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_secondFragment)
        }

        parentFragmentManager.setFragmentResultListener(
            "scoreResult", this
        ) { requestKey, result ->
            val newScore = result.getInt("score")
            val tvScore2TextView = rootView.findViewById<TextView>(R.id.tvScore2)
            val currentScore = tvScore2TextView.text.toString().removePrefix("Score: ").toInt()
            if (newScore > currentScore) {
                tvScore2TextView.text = "Score: $newScore"
            }
        }

        return rootView
    }
}



