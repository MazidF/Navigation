package com.example.netflix

import android.graphics.Bitmap

data class NetflixUser(val name: String, val family: String, val email: String) {
    val userName = name
    val image: Bitmap? = null
}
