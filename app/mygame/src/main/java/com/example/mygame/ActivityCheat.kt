package com.example.mygame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.mygame.Question.Companion.ANSWER
import com.example.mygame.Question.Companion.HAS_SEEN
import com.example.mygame.Question.Companion.QUESTION
import com.example.mygame.databinding.ActivityCheatBinding

class ActivityCheat : AppCompatActivity() {
    lateinit var binding: ActivityCheatBinding
    var userSeenTheAnswer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {

        val extra = intent!!.extras!!
        with(binding) {
            answerQuestion.text = extra.getString(QUESTION)
            answerAnswer.text = extra.getBoolean(ANSWER).toString()
            answerShowBtn.setOnClickListener {
                answerAnswer.visibility = View.VISIBLE
                userSeenTheAnswer = true
            }
        }
    }

    override fun onBackPressed() {
        setResult(10, Intent().apply {
            putExtra(HAS_SEEN, userSeenTheAnswer)
        })
        finish()
        super.onBackPressed()
    }
}
