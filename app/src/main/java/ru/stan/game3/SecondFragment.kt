package ru.stan.game3

import android.animation.ArgbEvaluator
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.stan.game3.models.BoardSize
import ru.stan.game3.models.MemoryGame

class SecondFragment : Fragment() {
    private lateinit var clRoot: ConstraintLayout
    private lateinit var tvNumPairs: TextView
    private lateinit var tvNumMoves: TextView
    private lateinit var adapter: MemoryBoardAdapter
    private lateinit var memoryGame: MemoryGame
    private var boardSize: BoardSize = BoardSize.EASY

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_second,
            container,
            false
        )
        clRoot = view.findViewById(R.id.clRoot)
        tvNumPairs = view.findViewById(R.id.tvNumPairs)
        tvNumMoves = view.findViewById(R.id.tvNumMoves)

        memoryGame = MemoryGame(boardSize)

        adapter = MemoryBoardAdapter(
            requireContext(), boardSize, memoryGame.cards, object :
                MemoryBoardAdapter.CardClickListener {
                override fun onCardClicked(position: Int) {
                    updateGameWithFlip(position)
                }
            }
        )

        val recyclerView: RecyclerView = view.findViewById(R.id.rvBoard)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), boardSize.getWidth())
        recyclerView.adapter = adapter

        return view
    }

    private fun updateGameWithFlip(position: Int) {
        if (memoryGame.haveWonGame()) {
            val message = getString(R.string.you_already_won)
            Snackbar.make(clRoot, message, Snackbar.LENGTH_LONG).show()
            return
        }
        if (memoryGame.isCardFaceUp(position)) {
            val message = getString(R.string.invalid_move)
            Snackbar.make(clRoot, message, Snackbar.LENGTH_SHORT).show()
            return
        }
        if (memoryGame.flipCard(position)) {
            Log.i(
                TAG,
                "Found a match! Number of pairs found: ${memoryGame.numPairsFound}"
            )
            val color = ArgbEvaluator().evaluate(
                memoryGame.numPairsFound.toFloat() / boardSize.getNumPairs(),
                ContextCompat.getColor(requireContext(), R.color.color_progress_none),
                ContextCompat.getColor(requireContext(), R.color.color_progress_full)
            ) as Int
            tvNumPairs.setTextColor(color)
            tvNumPairs.text =
                "Pairs: ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}"
            if (memoryGame.haveWonGame()) {
                val message = getString(R.string.congratulations_you_won)
                Snackbar.make(clRoot, message, Snackbar.LENGTH_LONG).show()
            }
        }
        tvNumMoves.text = "Moves: ${memoryGame.getNumMoves()}"
        adapter.notifyDataSetChanged()
    }
}