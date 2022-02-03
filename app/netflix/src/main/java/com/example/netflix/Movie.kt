package com.example.netflix

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView

data class Movie(val name: String, val index: Int) {
    var image: Bitmap? = null

    fun getView(context: Context, apply: (AppCompatImageView, View) -> AppCompatImageView): View {
        val view = View.inflate(context, R.layout.movie_layout, null)
        view.apply {
            findViewById<ImageView>(R.id.movie_image).setImageBitmap(image)
            findViewById<TextView>(R.id.movie_text).apply {
                text = name
            }
            apply(findViewById(R.id.movie_button), view)
        }
        return view
    }
}
