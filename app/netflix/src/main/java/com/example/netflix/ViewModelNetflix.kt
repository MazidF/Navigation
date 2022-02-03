package com.example.netflix

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelNetflix : ViewModel() {
    var user: MutableList<NetflixUser>? = null
    val hasRegistered = MutableLiveData(false)
    val movies by lazy {
        MutableLiveData<MutableList<View>>(mutableListOf())
    }
    val favoriteMovies by lazy {
        MutableLiveData<MutableList<View>>(mutableListOf())
    }
    val favoriteRemoved by lazy {
        MutableLiveData<View>()
    }

    fun getView(index: Int) = movies.value!![index]

    fun addFavorite(view: View) {
        favoriteMovies.value?.add(view)
    }

    fun removeFavorite(view: View) {
        favoriteMovies.value?.remove(view)
        favoriteRemoved.value = view
    }
}
