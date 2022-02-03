package com.example.mygame

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.mygame.Question.Companion.HAS_SEEN
import com.example.mygame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val controller by lazy {
        findNavController(R.id.container)
    }
    var question: Question? = null
    lateinit var binding: ActivityMainBinding
    lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        launcher()
        init()
    }

    private fun init() {
        NavigationUI.setupActionBarWithNavController(this, controller, binding.root)
        NavigationUI.setupWithNavController(binding.drawer, controller)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(controller, binding.root) || super.onSupportNavigateUp()
    }

    private fun launcher() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            question?.run {
                hasCheated = it.data!!.getBooleanExtra(HAS_SEEN, false)
            }
        }
    }
}
