package com.example.mygame

import android.os.Bundle
import android.view.Gravity
import android.content.Intent
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import android.annotation.SuppressLint
import androidx.core.view.GravityCompat
import androidx.navigation.ui.NavigationUI
import androidx.fragment.app.FragmentFactory
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.mygame.Question.Companion.HAS_SEEN
import androidx.activity.result.ActivityResultLauncher
import com.example.mygame.databinding.ActivityMainBinding
import com.example.mygame.databinding.LayoutHeaderBinding
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private val controller by lazy {
        (supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment)
            .findNavController()
    }
    lateinit var binding: ActivityMainBinding
    lateinit var header: LayoutHeaderBinding
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


        header = LayoutHeaderBinding.bind(binding.drawer.getHeaderView(0))
        with(viewModel) {
            incorrectLiveData.observe(this@MainActivity) {
                header.wrongAnswers.text = it.toString()
                if (it > correctLiveData.value!!) {
                    header.emoji.setImageResource(R.drawable.icon_sad)
                }
            }
            correctLiveData.observe(this@MainActivity) {
                header.rightAnswers.text = it.toString()
                if (it >= incorrectLiveData.value!!) {
                    header.emoji.setImageResource(R.drawable.icon_happy)
                }
            }
        }
    }

    @SuppressLint("RtlHardcoded")
    override fun onSupportNavigateUp(): Boolean {
        if (binding.root.isDrawerOpen(GravityCompat.START)) {
            binding.root.closeDrawer(Gravity.LEFT)
            return true // successful
        }
        return NavigationUI.navigateUp(controller, binding.root) || super.onSupportNavigateUp()
    }

    @SuppressLint("RtlHardcoded")
    override fun onBackPressed() {
        if (binding.root.isDrawerOpen(GravityCompat.START)) {
            binding.root.closeDrawer(Gravity.LEFT)
            return
        }
        super.onBackPressed()
    }

    private fun launcher() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.question.apply {
                hasCheated = it.data!!.getBooleanExtra(HAS_SEEN, false)
            }
        }
    }
}
