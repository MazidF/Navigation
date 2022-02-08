package com.example.netflix

import android.view.View
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class ViewModelNetflix : ViewModel() {
    val userChanged by lazy {
        MutableLiveData(false)
    }
    val allUsers by lazy{
        HashSet<String>()
    }
    var favoritesIndexList: List<Int>? = null
    var image = MutableLiveData<Bitmap>(null)
    val moveToLiveData = MutableLiveData(ICON.NONE)
    var user: NetflixUser? = null
    val hasRegistered = MutableLiveData(false)
    val favoriteMovies: MutableList<View> = mutableListOf()
    val favoriteRemoved by lazy {
        MutableLiveData<View>()
    }

    init {
        hasRegistered.observeForever {
            if (it) {
                if (allUsers.add(user!!.email)) {
                    Log.d("tag-tag", "user added")
                } else {
                    Log.d("tag-tag", "user loaded")
                }
            }
        }
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
