package com.example.netflix

import android.Manifest
import android.R.attr.bitmap
import android.R.attr.data
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.View.*
import android.widget.Toolbar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.netflix.ViewModelNetflix.Companion.ICON.*
import com.example.netflix.databinding.ActivityMainBinding
import com.example.netflix.databinding.UserLayoutBinding
import java.io.FileDescriptor
import java.io.IOException
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var root: DrawerLayout
    lateinit var menuItems: List<MenuItem>
    lateinit var launcher: ActivityResultLauncher<Intent>
    val model: ViewModelNetflix by viewModels()
    private val controller by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        launcherInit()
        setCustomFactory()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getPermission()
        init()
    }

    private fun getPermission() {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (!it) {
                finish()
            }
        }.launch(Manifest.permission.CAMERA)
    }

    private fun setCustomFactory() {
        supportFragmentManager.fragmentFactory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                if (loadFragmentClass(classLoader, className) == FragmentProfile::class.java) {
                    return FragmentProfile(launcher)
                }
                return super.instantiate(classLoader, className)
            }
        }
    }

    private fun launcherInit() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val intent = it.data ?: return@registerForActivityResult
            val bitmap = intent.data?.let { uri ->
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            } ?: intent.extras?.get("data") as Bitmap
            model.image.value = bitmap
        }
    }

    private fun init() {
        with(binding) {
            this@MainActivity.root = this.root
            menuItems = bottomNavigation.menu.children.toList()
            NavigationUI.setupActionBarWithNavController(this@MainActivity, controller, root)
            NavigationUI.setupWithNavController(bottomNavigation, controller)
            NavigationUI.setupWithNavController(drawer, controller)
            headerInit(drawer.getHeaderView(0))
        }
        model.moveToLiveData.observe(this) {
            val index = when(it) {
                HOME -> 0
                FAVORITE -> 1
                COMING_SOON -> 2
                PROFILE -> 3
                NONE -> return@observe
            }
            moveTo(index)
        }
    }

    private fun headerInit(header: View) {
        val binding = UserLayoutBinding.bind(header)
        with(binding) {
            userRegister.setOnClickListener {
                moveTo(3)
            }
            model.hasRegistered.observe(this@MainActivity) {
                if (it) {
                    userRegister.visibility = GONE
                    val user = model.user.value!!
                    userUsername.apply {
                        text = user.userName
                        visibility = VISIBLE
                    }
                    userEmail.apply {
                        text = user.email
                        visibility = VISIBLE
                    }
                    user.image?.let { bitmap ->
                        userImage.setImageBitmap(bitmap)
                    }
                } else {
                    userRegister.visibility = VISIBLE
                    userUsername.visibility = GONE
                    userEmail.visibility = GONE
                    userImage.setImageResource(R.drawable.icon_user)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        model.moveToLiveData.value = NONE
        // change it to NONE for preventing to changing item
    }

    private fun moveTo(index: Int) {
        NavigationUI.onNavDestinationSelected(menuItems[index], controller)
    }

    override fun onBackPressed() {
        if (!drawerHandler(false)) {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return drawerHandler() || /*NavigationUI.navigateUp(controller, root) ||*/ super.onSupportNavigateUp()
    }

    @SuppressLint("RtlHardcoded")
    private fun drawerHandler(open: Boolean = true): Boolean {
        if (root.isOpen) {
            root.close()
            return true // successful
        }
        if (open) {
            root.open()
        }
        return false
    }
}
