package ru.stan.game3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton

class HomeFragment : Fragment() {
    lateinit var rootView: View
    var oldTvScore2Value: Int = 0
    var isEasyButtonChecked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)

        rootView.findViewById<MaterialButton>(R.id.rbEasy).setOnClickListener {
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

        if (savedInstanceState != null) {
            val savedScore = savedInstanceState.getInt("score")
            rootView.findViewById<TextView>(R.id.tvScore2).text = "Score: $savedScore"
            isEasyButtonChecked = savedInstanceState.getBoolean("isEasyButtonChecked")
        }

        rootView.findViewById<MaterialButton>(R.id.rbEasy).isChecked = isEasyButtonChecked

        rootView.findViewById<MaterialButton>(R.id.action_homeFragment_to_secondFragment)

        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val currentScore = rootView.findViewById<TextView>(R.id.tvScore2).text.toString().removePrefix("Score: ").toInt()
        outState.putInt("score", currentScore)
        outState.putBoolean("isEasyButtonChecked", rootView.findViewById<MaterialButton>(R.id.rbEasy).isChecked)
    }
}




