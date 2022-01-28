package com.example.navigationproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.navigationproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val controller by lazy {
        findNavController(R.id.mainContainer)
    }
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NavigationUI.setupActionBarWithNavController(this, controller, binding.drawer)
        NavigationUI.setupWithNavController(binding.navigationView, controller)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (binding.drawer.isOpen) {
            binding.drawer.closeDrawer(GravityCompat.START)
            return true // successful
        }
        return NavigationUI.navigateUp(controller, binding.drawer)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, R.id.fragmentThird, 0, "ThirdFragment")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, controller) ||
                super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        binding.run {
            if (drawer.isOpen) {
                drawer.closeDrawer(GravityCompat.START)
                return
            }
        }
        super.onBackPressed()
    }

    fun call(item: MenuItem) {
        startActivity(Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("tel:09123456789")
        })
    }
}

