package com.example.navigationproject

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.navigationproject.databinding.FragmentSecondBinding

class FragmentSecond : Fragment(R.layout.fragment_second) {
    private val controller by lazy {
        findNavController()
    }
    lateinit var binding: FragmentSecondBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSecondBinding.bind(view)
        binding.secondBtn.setOnClickListener {
            controller.navigate(FragmentSecondDirections.actionFragmentSecondToFragmentThird())
        }
    }
}
