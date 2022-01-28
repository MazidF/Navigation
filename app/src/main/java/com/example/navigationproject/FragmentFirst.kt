package com.example.navigationproject

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.navigationproject.databinding.FragmentFirstBinding

class FragmentFirst : Fragment(R.layout.fragment_first) {
    val controller by lazy {
        findNavController()
    }
    lateinit var binding: FragmentFirstBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFirstBinding.bind(view)
        binding.firstBtn.setOnClickListener {
            controller.navigate(FragmentFirstDirections.actionFragmentFirstToFragmentSecond())
        }
    }

}