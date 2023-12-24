package ru.stan.game3

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

class WinningFragment : Fragment() {
    private lateinit var tvScore: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_winning, container, false)
        tvScore = view.findViewById(R.id.tvScore)
        val score = arguments?.getInt("score") ?: 0
        tvScore.text = "Score: $score"

        parentFragmentManager.setFragmentResult(
            "scoreResult", bundleOf("score" to score)
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<Button>(R.id.btnBack).setOnClickListener {
            findNavController().navigateUp()
            Handler().postDelayed(
                { fragmentManager?.beginTransaction()?.remove(this)?.commit() }, 1
            )
        }
    }
}