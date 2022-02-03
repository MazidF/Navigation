package com.example.mygame

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mygame.ANSWER.*
import kotlin.random.Random

class ViewModelGame: ViewModel() {
    private var index = MutableLiveData(0)
    val correctLiveData by lazy {
        MutableLiveData(0)
    }
    val incorrectLiveData by lazy {
        MutableLiveData(0)
    }
    var questions: List<Question> = List(10) {
        Question("Question${it + 1}: Is it Ture?", Random.nextBoolean())
    }
    lateinit var question: Question

    fun answer(context: Context, answer: Boolean) {
        question.answer(context, answer).run {
            when(this) {
                INCORRECT -> {
                    incorrectLiveData.value = incorrectLiveData.value!! + 1
                }
                else -> {
                    correctLiveData.value = correctLiveData.value!! + 1
                }
            }
        }
    }

    fun setQuestion(): Pair<Question, Int> {
        val index = index.value!!
        question = questions[index]
        return Pair(question, index)
    }

    fun next() : Boolean {
        val value = index.value!!
        if (value < 9) {
            index.value = value + 1
            return true
        }
        return false
    }

    fun prev() : Boolean {
        val value = index.value!!
        if (0 < value) {
            index.value = value - 1
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

    fun answer(context: Context, answer: Boolean): ANSWER? {
        if (hasAnswered) return null
        hasAnswered = true
        val result = if (answer == this.answer) {
            if (hasCheated) {
                CHEAT
            } else {
                CORRECT
            }
        } else {
            INCORRECT
        }
        Toast.makeText(context, result.msg(), Toast.LENGTH_SHORT).show()
        return result
    }

    fun cheat(launcher: ActivityResultLauncher<Intent>, context: Context) {
        launcher.launch(Intent(context, ActivityCheat::class.java).apply {
            putExtra(QUESTION, question)
            putExtra(ANSWER, answer)
        })
    }
}

enum class ANSWER {
    CORRECT {
        override fun msg() = "Correct!!!"
    }, INCORRECT {
        override fun msg() = "Incorrect!!!"
    }, CHEAT {
        override fun msg() = "Cheating is wrong!!!"
    };
    abstract fun msg(): String
}
