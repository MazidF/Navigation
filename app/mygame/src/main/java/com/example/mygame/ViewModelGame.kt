package com.example.mygame

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class ViewModelGame: ViewModel() {
    private var index = 0
    var question: Question
    var questions: List<Question>
    var fragmentGame: FragmentGame? = null

    init {
        questions = List(10) {
            Question("Question${it + 1}: Is it Ture?", Random.nextBoolean())
        }
        question = questions[0]
    }

    fun setFragment(fragmentGame: FragmentGame) {
        this.fragmentGame = fragmentGame
        fragmentGame.run {
            with(binding) {
                gameNext.setOnClickListener {
                    next()
                }
                gamePrevious.setOnClickListener {
                    prev()
                }
                gameCheat.setOnClickListener {
                    cheat()
                }
                gameTrue.setOnClickListener {
                    answer(true)
                }
                gameFalse.setOnClickListener {
                    answer(false)
                }
            }
        }
        setQuestion(index)
    }

    fun remove() {
        fragmentGame = null
    }

    private fun answer(answer: Boolean) {
        fragmentGame?.run {
            question.answer(requireContext(), answer)
            setEnable(false)
        }
    }

    private fun setQuestion(index: Int) {
        question = questions[index]
        fragmentGame?.run {
            with(binding) {
                gameQuestion.text = question.question
                setEnable(!question.hasAnswered)
                if (question.hasCheated) {
                    Toast.makeText(requireContext(), "Shame!!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setEnable(isEnable: Boolean) {
        fragmentGame?.run {
            with(binding) {
                gameFalse.isEnabled = isEnable
                gameTrue.isEnabled = isEnable
                gameCheat.isEnabled = isEnable
            }
        }
    }

    private fun next() {
        if (index < 9) {
            index++
            setQuestion(index)
        }
    }

    private fun prev() {
        if (0 < index) {
            index--
            setQuestion(index)
        }
    }

    private fun cheat() {
        fragmentGame?.run {
            question.cheat(app)
        }
    }
}

data class Question(val question: String, val answer: Boolean) {

    companion object {
        const val QUESTION = "question"
        const val HAS_SEEN = "hasSeen"
        const val ANSWER = "answer"
    }

    var hasCheated = false
    var hasAnswered = false

    fun answer(context: Context, answer: Boolean) {
        if (hasAnswered) return
        hasAnswered = true
        val msg = if (answer == this.answer) {
            if (hasCheated) {
                "Cheating is wrong!!!"
            } else {
                "Correct!!!"
            }
        } else {
            "Incorrect!!!"
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun cheat(app: MainActivity) {
        app.question = this
        app.launcher.launch(Intent(app, ActivityCheat::class.java).apply {
            putExtra(QUESTION, question)
            putExtra(ANSWER, answer)
        })
    }
}
