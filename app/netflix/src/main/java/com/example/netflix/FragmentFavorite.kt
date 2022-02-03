package com.example.netflix

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.netflix.databinding.FragmentFavoriteBinding

class FragmentFavorite : Fragment(R.layout.fragment_favorite) {
    val model: ViewModelNetflix by activityViewModels()
    lateinit var binding: FragmentFavoriteBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteBinding.bind(view)
        init()
    }

    private fun init() {
        with(binding.favoriteList) {
            with(model) {
                favoriteRemoved.observe(viewLifecycleOwner) {
                    removeView(it)
                }

                for (view in favoriteMovies.value!!) {
                    addView(view)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        binding.favoriteList.removeAllViews()
    }
}
