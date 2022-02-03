package com.example.navigationproject

import androidx.lifecycle.ViewModel

class ThirdViewModel : ViewModel() {
    var myFragment: FragmentThird? = null
    var counter = 0

    fun setFragment(fragment: FragmentThird) {
        myFragment = fragment
    }

    fun increase() {
        myFragment?.run {
            binding.countViewer.text = (++counter).toString()
        }
    }

    fun decrease() {
        myFragment?.run {
            binding.countViewer.text = (--counter).toString()
        }
    }
}
