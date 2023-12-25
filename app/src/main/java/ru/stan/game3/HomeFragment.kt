package ru.stan.game3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {
    lateinit var rootView: View
    private var currentScore = 0 // Переменная для хранения текущего значения tvScore2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)
        rootView.findViewById<Button>(R.id.rbEasy).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_secondFragment)
        }

        // Устанавливаем слушатель для получения результатов
        parentFragmentManager.setFragmentResultListener(
            "scoreResult", this
        ) { requestKey, result ->
            val newScore = result.getInt("score")
            // Обновляем tvScore2 в соответствии с условиями
            if (newScore > currentScore) {
                rootView.findViewById<TextView>(R.id.tvScore2).text = "Score: $newScore"
                currentScore = newScore
            } else if (newScore < currentScore) {
                rootView.findViewById<TextView>(R.id.tvScore2).text = "Score: $currentScore"
            } else {
                rootView.findViewById<TextView>(R.id.tvScore2).text = "Score: $newScore"
            }
        }

        return rootView
    }
}





