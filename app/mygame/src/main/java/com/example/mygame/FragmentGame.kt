package com.example.mygame

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mygame.databinding.FragmentGameBinding

class FragmentGame : Fragment(R.layout.fragment_game) {
    lateinit var binding: FragmentGameBinding
    val viewModel: ViewModelGame by viewModels()
    lateinit var app: MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGameBinding.bind(view)
        viewModel.setFragment(this)
    }

    override fun onDestroy() {
        viewModel.remove()
        super.onDestroy()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        app = context as MainActivity
    }
}
