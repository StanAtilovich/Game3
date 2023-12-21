package ru.stan.game3

import android.animation.ArgbEvaluator
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
    private lateinit var rvBoard: RecyclerView

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

        rvBoard = view.findViewById(R.id.rvBoard)

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
                findNavController().navigate(R.id.action_secondFragment_to_winningFragment)
            }
        }
        tvNumMoves.text = "Moves: ${memoryGame.getNumMoves()}"
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_refresh -> {
                if (::memoryGame.isInitialized && memoryGame.getNumMoves() > 0 && !memoryGame.haveWonGame()) {
                    showAlertDialog("Quit your current game?", null, View.OnClickListener { setUpBoard() })
                } else {
                    setUpBoard()
                }
                return true
            }
            R.id.mi_new_size -> {
                showNewSizeDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }




    fun showNewSizeDialog() {
        val boardSizeView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_board_size, null)
        val radioGroupSize = boardSizeView.findViewById<RadioGroup>(R.id.radioGroup)
        when (boardSize) {
            BoardSize.EASY -> radioGroupSize.check(R.id.rbEasy)
            BoardSize.MEDIUM -> radioGroupSize.check(R.id.rbMedium)
            BoardSize.HARD -> radioGroupSize.check(R.id.rbHard)
        }
        showAlertDialog("Choose new size", boardSizeView, View.OnClickListener {
            boardSize = when (radioGroupSize.checkedRadioButtonId) {
                R.id.rbEasy -> BoardSize.EASY
                R.id.rbMedium -> BoardSize.MEDIUM
                else -> BoardSize.HARD
            }
            setUpBoard()
        })
    }
    private fun showAlertDialog(title: String, view: View?, positiveClickListener: View.OnClickListener) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK") {_, _->
                positiveClickListener.onClick(null)
            }.show()
    }
    private fun setUpBoard() {
        when (boardSize) {
            BoardSize.EASY -> {
                tvNumMoves.text = "Easy: 4 x 2"
                tvNumPairs.text = "Pairs: 0 / 4"
            }
            BoardSize.MEDIUM -> {
                tvNumMoves.text = "Medium: 6 x 3"
                tvNumPairs.text = "Pairs: 0 / 9"
            }
            BoardSize.HARD -> {
                tvNumMoves.text = "Hard: 6 x 4"
                tvNumPairs.text = "Pairs: 0 / 12"
            }
        }
        tvNumPairs.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_progress_none))
        memoryGame = MemoryGame(boardSize)

        adapter = MemoryBoardAdapter(requireContext(), boardSize, memoryGame.cards, object: MemoryBoardAdapter.CardClickListener{
            override fun onCardClicked(position: Int) {
                updateGameWithFlip(position)
            }

        })
        rvBoard.adapter = adapter
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(requireContext(), boardSize.getWidth())
    }

}