package com.example.mygame

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class ViewModelGame: ViewModel() {
    private var index = 0
    var question: Question
    var questions: List<Question> = List(10) {
        Question("Question${it + 1}: Is it Ture?", Random.nextBoolean())
    }

    init {
        question = questions[0]
    }

    fun answer(context: Context, answer: Boolean) {
        question.answer(context, answer)
    }

    fun setQuestion(): Pair<Question, Int> {
        question = questions[index]
        return Pair(question, index)
    }

    fun next() : Boolean {
        if (index < 9) {
            index++
            return true
        }
        return false
    }

    fun prev() : Boolean {
        if (0 < index) {
            index--
            return true
        }
        return false
    }

    fun cheat(launcher: ActivityResultLauncher<Intent>, context: Context) {
        question.cheat(launcher, context)
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

    fun cheat(launcher: ActivityResultLauncher<Intent>, context: Context) {
        launcher.launch(Intent(context, ActivityCheat::class.java).apply {
            putExtra(QUESTION, question)
            putExtra(ANSWER, answer)
        })
    }
}
