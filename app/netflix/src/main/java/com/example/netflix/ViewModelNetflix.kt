package com.example.netflix

import android.view.View
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class ViewModelNetflix : ViewModel() {
    var favoritesIndexList: MutableList<Int>? = null
    var image = MutableLiveData<Bitmap>(null)
    val moveToLiveData = MutableLiveData(ICON.NONE)
    var user: NetflixUser? = null
    val hasRegistered = MutableLiveData(false)
    val favoriteMovies: MutableList<View> = mutableListOf()
    val favoriteRemoved by lazy {
        MutableLiveData<View>()
    }

    fun addFavorite(view: View) {
        favoriteMovies.add(view)
    }

    fun removeFavorite(view: View) {
        favoriteMovies.remove(view)
        favoriteRemoved.value = view
    }

    companion object {
        enum class ICON {
            HOME, FAVORITE, COMING_SOON, PROFILE, NONE;
        }
    }
}
