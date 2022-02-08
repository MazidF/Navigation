package com.example.netflix

import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class ViewModelSaveLayout : ViewModel() {
    val linearLayouts by lazy {
//        MutableLiveData<MutableList<LinearLayout>>(mutableListOf())
        mutableListOf<LinearLayout>()
    }

    val views by lazy {
        MutableLiveData<MutableList<View>>(mutableListOf())
    }

    var binding: Any? = null

    fun addLinear(linearLayout: LinearLayout) {
        linearLayouts.add(linearLayout)
    }

    fun addView(view: View) {
        views.value!!.add(view)
    }
}
