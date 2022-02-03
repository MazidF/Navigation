package com.example.mygame

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.content.Intent
import android.view.View.VISIBLE
import android.view.View.INVISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.activity.result.ActivityResultLauncher
import com.example.mygame.databinding.FragmentGameBinding

class FragmentGame(private val launcher: ActivityResultLauncher<Intent>) : Fragment(R.layout.fragment_game) {
    lateinit var binding: FragmentGameBinding
    private val viewModel: ViewModelGame by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGameBinding.bind(view)
        init()
    }

    private fun init() {
        with(binding) {
            gameNext.setOnClickListener {
                if (viewModel.next()) {
                    setQuestion()
                }
            }
            gamePrevious.setOnClickListener {
                if (viewModel.prev()) {
                    setQuestion()
                }
            }
            gameCheat.setOnClickListener {
                viewModel.cheat(launcher, requireContext())
            }
            gameTrue.setOnClickListener {
                answer(true)
            }
            gameFalse.setOnClickListener {
                answer(false)
            }
        }
        setQuestion()
    }

    private fun setQuestion() {
        val (question, index) = viewModel.setQuestion()
        with(binding) {
            gameQuestion.text = question.question
            setEnable(!question.hasAnswered)
            if (question.hasCheated) {
                Toast.makeText(requireContext(), "Shame!!!", Toast.LENGTH_SHORT).show()
            }
            when(index) {
                0 -> gamePrevious.visibility = INVISIBLE
                1 -> gamePrevious.visibility = VISIBLE
                8 -> gameNext.visibility = VISIBLE
                9 -> gameNext.visibility = INVISIBLE
            }
        }
    }

    private fun setEnable(isEnable: Boolean) {
        with(binding) {
            gameFalse.isEnabled = isEnable
            gameTrue.isEnabled = isEnable
            gameCheat.isEnabled = isEnable
        }
    }

    private fun answer(answer: Boolean) {
        viewModel.answer(requireContext(), answer)
        setEnable(false)
    }
}
