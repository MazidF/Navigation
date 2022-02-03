package com.example.navigationproject

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.navigationproject.databinding.FragmentThirdBinding

class FragmentThird : Fragment(R.layout.fragment_third) {
    lateinit var binding: FragmentThirdBinding
    val viewModel: ThirdViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentThirdBinding.bind(view)
        viewModel.setFragment(this)
        binding.run {
            binding.countViewer.text = viewModel.counter.toString()
            buttonMinus.setOnClickListener {
                viewModel.decrease()
            }
            buttonPlus.setOnClickListener {
                viewModel.increase()
            }
        }
    }
}
