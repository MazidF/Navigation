package com.example.navigationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {
    private val controller by lazy {
        findNavController(R.id.mainContainer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NavigationUI.setupActionBarWithNavController(this, controller)
    }

    override fun onSupportNavigateUp(): Boolean {
        return controller.navigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, R.id.fragmentThird, 0, "ThirdFragment")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, controller) ||
                super.onOptionsItemSelected(item)
    }
}
