package com.example.netflix

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams.*
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import com.example.netflix.databinding.FragmentSoonBinding

class FragmentComingSoon : Fragment(R.layout.fragment_soon) {
    lateinit var binding: FragmentSoonBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSoonBinding.bind(view)
        init()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun init() {
        with(binding.comingSoonList) {
            val bitmap = context.resources.getDrawable(R.drawable.icon_soon).toBitmap(50, 50)
            val params = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1f).apply {
                setMargins(20)
            }
            for (i in 0 .. 2) {
                addView(viewMaker("ComingSoon ${i + 1}", bitmap, params))
            }
            gravity = Gravity.CENTER_HORIZONTAL
        }
    }

    private fun viewMaker(name: String, image: Bitmap, params: LinearLayout.LayoutParams): View {
        return View.inflate(requireContext(), R.layout.movie_layout, null).apply {
            this.layoutParams = params

            findViewById<ImageView>(R.id.movie_image).setImageBitmap(image)
            findViewById<TextView>(R.id.movie_text).apply {
                text = name
            }
            findViewById<AppCompatImageView>(R.id.movie_button).apply {
                setImageResource(R.drawable.icon_share)
                setOnClickListener {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, name)
                        type = "text/plain"
                    }
                    val chooser = Intent.createChooser(intent, "Share Movie")
                    startActivity(chooser)
                }
            }
        }
    }
}
