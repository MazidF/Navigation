package com.example.netflix

import android.graphics.Bitmap

data class NetflixUser(val name: String, val family: String, val email: String) {
    var userName = "$name $family"
    var image: Bitmap? = null
}
