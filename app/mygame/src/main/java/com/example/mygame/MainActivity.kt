package com.example.mygame

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.mygame.Question.Companion.HAS_SEEN
import com.example.mygame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val controller by lazy {
        findNavController(R.id.container)
    }
    lateinit var binding: ActivityMainBinding
    lateinit var launcher: ActivityResultLauncher<Intent>
    val viewModel: ViewModelGame by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        launcher()
        supportFragmentManager.fragmentFactory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return when(loadFragmentClass(classLoader, className)) {
                    FragmentGame::class.java -> {
                        FragmentGame(launcher)
                    }
                    else -> super.instantiate(classLoader, className)
                }
            }
        }

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            viewModel.question.apply {
                hasCheated = it.data!!.getBooleanExtra(HAS_SEEN, false)
            }
        }
    }
}
