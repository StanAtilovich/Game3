package ru.stan.game3

import android.animation.ArgbEvaluator
import android.content.ContentValues.TAG
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.CountDownTimer
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.stan.game3.models.BoardSize
import ru.stan.game3.models.MemoryGame

class SecondFragment : Fragment() {
    private lateinit var tvTimer: TextView
    private lateinit var clRoot: ConstraintLayout
    private lateinit var tvNumPairs: TextView
    private lateinit var tvNumMoves: TextView
    private lateinit var adapter: MemoryBoardAdapter
    private lateinit var memoryGame: MemoryGame
    private var boardSize: BoardSize = BoardSize.EASY
    private lateinit var rvBoard: RecyclerView

    private var timeLeft: Long = 0
    private var score = 100
    private lateinit var timer: CountDownTimer

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("timeLeft", timeLeft)
    }


    private fun startTimer(initialTime: Long) {
        timer = object : CountDownTimer(initialTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                val secondsLeft = millisUntilFinished / 1000
                tvTimer.text = "Time: $secondsLeft"
                updateScore(secondsLeft)
            }

            override fun onFinish() {
                tvTimer.text = "Time's up! Score: $score"
                navigateToWinningFragment(score)
            }
        }
        timer.start()
    }
    private fun updateScore(secondsLeft: Long) {
        if (secondsLeft <= 110) {
            score = 100
        }
        if (secondsLeft <= 105) {
            score = 95
        }
        if (secondsLeft <= 100) {
            score = 90
        }
        if (secondsLeft <= 95) {
            score = 85
        }
        if (secondsLeft <= 90) {
            score = 80
        }
        if (secondsLeft <= 85) {
            score = 75
        }
        if (secondsLeft <= 80) {
            score = 70
        }
        if (secondsLeft <= 75) {
            score = 65
        }
        if (secondsLeft <= 70) {
            score = 60
        }
        if (secondsLeft <= 65) {
            score = 55
        }
        if (secondsLeft <= 60) {
            score = 50
        }
        if (secondsLeft <= 55) {
            score = 45
        }
        if (secondsLeft <= 50) {
            score = 40
        }
        if (secondsLeft <= 55) {
            score = 35
        }
        if (secondsLeft <= 45) {
            score = 30
        }
        if (secondsLeft <= 40) {
            score = 25
        }
        if (secondsLeft <= 35) {
            score = 20
        }
    }
    private fun navigateToWinningFragment(score: Int) {
        val bundle = Bundle()
        bundle.putInt(ARG_SCORE, score)
        val winningFragment = WinningFragment()
        winningFragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, winningFragment, "winningFragment").commit()
        timer.cancel()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTimer = view.findViewById(R.id.tvTimer)

        if (savedInstanceState != null) {
            timeLeft = savedInstanceState.getLong("timeLeft")
            startTimer(timeLeft)
        } else {
            startTimer(110000)
        }
    }


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
                navigateToWinningFragment(score)
//                navigateToHomeFragment(score)
            }
        }
        tvNumMoves.text = "Moves: ${memoryGame.getNumMoves()}"
        adapter.notifyDataSetChanged()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_refresh -> {
                if (::memoryGame.isInitialized && memoryGame.getNumMoves() > 0 && !memoryGame.haveWonGame()) {
                    showAlertDialog(
                        "Quit your current game?",
                        null,
                        View.OnClickListener { setUpBoard() })
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

    private fun showAlertDialog(
        title: String,
        view: View?,
        positiveClickListener: View.OnClickListener
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK") { _, _ ->
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
        tvNumPairs.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.color_progress_none
            )
        )
        memoryGame = MemoryGame(boardSize)

        adapter = MemoryBoardAdapter(
            requireContext(),
            boardSize,
            memoryGame.cards,
            object : MemoryBoardAdapter.CardClickListener {
                override fun onCardClicked(position: Int) {
                    updateGameWithFlip(position)
                }

            })
        rvBoard.adapter = adapter
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(requireContext(), boardSize.getWidth())
    }

    companion object {
        const val ARG_SCORE = "score"
    }
}